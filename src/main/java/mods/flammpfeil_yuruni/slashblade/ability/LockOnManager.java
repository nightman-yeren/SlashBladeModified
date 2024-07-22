package mods.flammpfeil_yuruni.slashblade.ability;

import mods.flammpfeil_yuruni.slashblade.capability.inputstate.CapabilityInputState;
import mods.flammpfeil_yuruni.slashblade.entity.IShootable;
import mods.flammpfeil_yuruni.slashblade.event.InputCommandEvent;
import mods.flammpfeil_yuruni.slashblade.gamerules.SlashBladeHitRule;
import mods.flammpfeil_yuruni.slashblade.item.ItemSlashBlade;
import mods.flammpfeil_yuruni.slashblade.util.InputCommand;
import mods.flammpfeil_yuruni.slashblade.util.RayTraceHelper;
import mods.flammpfeil_yuruni.slashblade.util.TargetSelector;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.ThrownEgg;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.entity.PartEntity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static mods.flammpfeil_yuruni.slashblade.util.ExcludedPlayers.isPlayerInList;

public class LockOnManager {
    private static final class SingletonHolder {
        private static final LockOnManager instance = new LockOnManager();
    }

    public static LockOnManager getInstance() {
        return SingletonHolder.instance;
    }

    private LockOnManager() {
    }

    public void register() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onInputChange(InputCommandEvent event) {
        if(event.getOld().contains(InputCommand.SNEAK) == event.getCurrent().contains(InputCommand.SNEAK)) return;

        ServerPlayer player = event.getEntity();
        //set target
        ItemStack stack = event.getEntity().getMainHandItem();
        if (stack.isEmpty()) return;
        if (!(stack.getItem() instanceof ItemSlashBlade)) return;

        Entity targetEntity;

        if((event.getOld().contains(InputCommand.SNEAK) && !event.getCurrent().contains(InputCommand.SNEAK))){
            //remove target
            targetEntity = null;
        }else{
            //search target

            Optional<HitResult> result = RayTraceHelper.rayTrace(player.level(), player, player.getEyePosition(1.0f) , player.getLookAngle(), 40,40, (e)->true);
            Optional<Entity> foundEntity = result
                    .filter(r->r.getType() == HitResult.Type.ENTITY)
                    .filter(r->{
                        EntityHitResult er = (EntityHitResult)r;
                        Entity target = ((EntityHitResult) r).getEntity();

                        if(target instanceof PartEntity){
                            target = ((PartEntity) target).getParent();
                        }

                        boolean isMatch = true;

                        if(target instanceof LivingEntity)
                            isMatch = TargetSelector.lockon_focus.test(player, (LivingEntity)target);

                        if(target instanceof IShootable)
                            isMatch = false;

                        return isMatch;
                    }).map(r->((EntityHitResult) r).getEntity());
            if(!foundEntity.isPresent()) {
                List<LivingEntity> entities = player.level().getNearbyEntities(
                        LivingEntity.class,
                        TargetSelector.lockon,
                        player,
                        player.getBoundingBox().inflate(12.0D, 6.0D, 12.0D));

                foundEntity = entities.stream().map(s->(Entity)s).min(Comparator.comparingDouble(e -> e.distanceToSqr(player)));
                //Exceptions for de-selecting the entity
                if (foundEntity.isPresent()) {
                    //if (foundEntity.get().look)
                    if (!player.hasLineOfSight(foundEntity.get())) foundEntity = Optional.empty();
                    if (foundEntity.isPresent() && foundEntity.get() instanceof ItemEntity) foundEntity = Optional.empty();
                    if (foundEntity.isPresent() && foundEntity.get() instanceof ExperienceOrb) foundEntity = Optional.empty();
                    if (foundEntity.isPresent() && foundEntity.get() instanceof ItemFrame) foundEntity = Optional.empty();
                    if (foundEntity.isPresent() && foundEntity.get() instanceof Minecart) foundEntity = Optional.empty();
                    if (foundEntity.isPresent() && foundEntity.get() instanceof Arrow) foundEntity = Optional.empty();
                    if (foundEntity.isPresent() && foundEntity.get() instanceof ThrownPotion) foundEntity = Optional.empty();
                    if (foundEntity.isPresent() && foundEntity.get() instanceof ThrownEgg) foundEntity = Optional.empty();
                    if (foundEntity.isPresent() && foundEntity.get() instanceof Player) {
                        if (isPlayerInList(player, (Player) foundEntity.get()) || !SlashBladeHitRule.isEnabled(foundEntity.get().level(), SlashBladeHitRule.SLASHBLADE_HITPLAYER)) {
                            foundEntity = Optional.empty();
                        }
                    }
                    if (foundEntity.isPresent() && foundEntity.get() instanceof Animal && !SlashBladeHitRule.isEnabled(foundEntity.get().level(), SlashBladeHitRule.SLASHBLADE_HITPASSIVE)) {
                        foundEntity = Optional.empty();
                    }
                }
            }

            targetEntity = foundEntity
                    .map(e-> (e instanceof PartEntity) ? ((PartEntity) e).getParent() : e)
                    .orElse(null);

        }

        stack.getCapability(ItemSlashBlade.BLADESTATE).ifPresent(s -> {
            s.setTargetEntityId(targetEntity);
        });

    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onEntityUpdate(TickEvent.RenderTickEvent event) {
        if(event.phase != TickEvent.Phase.START) return;

        if(Minecraft.getInstance().player == null) return;

        LocalPlayer player = Minecraft.getInstance().player;

        ItemStack stack = player.getMainHandItem();
        if (stack.isEmpty()) return;
        if (!(stack.getItem() instanceof ItemSlashBlade)) return;

        stack.getCapability(ItemSlashBlade.BLADESTATE).ifPresent(s -> {

            Entity target = s.getTargetEntity(player.level());

            //Player check
            if (target instanceof Player) {
                if (isPlayerInList(player, (Player)target)) {
                    target = null;
                }
            }
            //Hit rule check
            if (target instanceof Animal && !SlashBladeHitRule.isEnabled(target.level(), SlashBladeHitRule.SLASHBLADE_HITPASSIVE)) {
                target = null;
            }
            if (target instanceof Player && !SlashBladeHitRule.isEnabled(target.level(), SlashBladeHitRule.SLASHBLADE_HITPLAYER)) {
                target = null;
            }
            if (target == null) return;
            if (!player.hasLineOfSight(target)) return;
            if (target instanceof ItemEntity) return;
            if (target instanceof ExperienceOrb) return;
            if (target instanceof ItemFrame) return;
            if (target instanceof Minecart) return;
            if (target instanceof Arrow) return;
            if (target instanceof ThrownPotion) return;
            if (target instanceof ThrownEgg) return;
            if(!target.isAlive()) return;

            if(!player.level().isClientSide) return;
            if(!player.getCapability(CapabilityInputState.INPUT_STATE).filter(input->input.getCommands().contains(InputCommand.SNEAK)).isPresent()) return;


            float partialTicks = Minecraft.getInstance().getFrameTime();

            float oldYawHead = player.yHeadRot;
            float oldYawOffset = player.yBodyRot;
            float oldPitch = player.getXRot();
            float oldYaw = player.getYRot();

            float prevYawHead = player.yHeadRotO;
            float prevYawOffset = player.yBodyRotO;
            float prevYaw = player.yRotO;
            float prevPitch = player.xRotO;

            player.lookAt(EntityAnchorArgument.Anchor.EYES, target.position().add(0,target.getEyeHeight() / 2.0,0));

            float step = 0.125f * partialTicks;

            step *= (float) Math.min(1.0f ,Math.abs(Mth.wrapDegrees(oldYaw - player.yHeadRot) * 0.5));

            player.setXRot(Mth.rotLerp(step,oldPitch , player.getXRot()));
            player.setYRot(Mth.rotLerp(step, oldYaw , player.getYRot()));
            player.setYHeadRot(Mth.rotLerp(step, oldYawHead , player.getYHeadRot()));

            player.yBodyRot = oldYawOffset;

            player.yBodyRotO = prevYawOffset;
            player.yHeadRotO = prevYawHead;
            player.yRotO = prevYaw;
            player.xRotO = prevPitch;
        });
    }

}
