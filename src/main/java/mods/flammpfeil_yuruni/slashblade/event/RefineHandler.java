package mods.flammpfeil_yuruni.slashblade.event;

import mods.flammpfeil_yuruni.slashblade.SlashBlade;
import mods.flammpfeil_yuruni.slashblade.item.ItemSlashBlade;
import mods.flammpfeil_yuruni.slashblade.util.AdvancementHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.player.AnvilRepairEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class RefineHandler {
    private static final class SingletonHolder {
        private static final RefineHandler instance = new RefineHandler();
    }
    public static RefineHandler getInstance() {
        return SingletonHolder.instance;
    }
    private RefineHandler(){}
    public void register(){
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onAnvilUpdateEvent(AnvilUpdateEvent event){
        if(!event.getOutput().isEmpty()) return;

        ItemStack base = event.getLeft();
        ItemStack material = event.getRight();

        if(base.isEmpty()) return;
        if(!(base.getItem() instanceof ItemSlashBlade)) return;
        if(material.isEmpty()) return;

        boolean isRepairable = base.getItem().isValidRepairItem(base,material);

        if (!isRepairable) {
            //Including the nether star
            if (!material.is(Items.NETHER_STAR)) {
                return;
            }
        }

        int level = material.getEnchantmentValue();

        if(level < 0) return;

        ItemStack result = base.copy();

        int refineLimit = Math.max(10, level);

        int cost = 0;
        while(cost < material.getCount()){
            cost ++;

            float damage = result.getCapability(ItemSlashBlade.BLADESTATE).map(s->{
                s.setDamage(s.getDamage() - (0.2f + 0.05f * level));
                if(s.getRefine() < refineLimit)
                    if (!(s.getRefine() == 500)) {
                        s.setRefine(s.getRefine() + 1);
                    }
                //Round attack damage
                float attackToAdd = ((float) s.getRefine() / 100);
                BigDecimal attackString = new BigDecimal(String.valueOf(attackToAdd));
                BigDecimal attackStringRounded;
                if (attackString.setScale(1, RoundingMode.DOWN).floatValue() == 5F) {//If base attack damage is increased 5 times
                    //Check if the refine item is a nether star
                    if (material.is(Items.NETHER_STAR)) {
                        attackStringRounded = new BigDecimal("5").setScale(1, RoundingMode.DOWN);
                        s.setSourceBaseAttackModifier(s.getSourceBaseAttackModifier() + 1);
                        s.setRefine(501);
                    } else {
                        attackStringRounded = new BigDecimal("5").setScale(1, RoundingMode.DOWN);
                    }
                } else if (attackString.setScale(1, RoundingMode.DOWN).floatValue() >= 10F) {
                    attackStringRounded = new BigDecimal("10").setScale(1, RoundingMode.DOWN);
                    s.setRefine(1000);
                } else {
                    attackStringRounded = attackString.setScale(1, RoundingMode.DOWN);
                }
                s.setBaseAttackModifier(s.getSourceBaseAttackModifier() + attackStringRounded.floatValue());
                return s.getDamage();
            }).orElse(0f);

            if(damage <= 0f) break;
        }

        event.setMaterialCost(cost);
        int levelCostBase = 1;
        event.setCost(levelCostBase * cost);
        event.setOutput(result);
    }

    static private final ResourceLocation REFINE = new ResourceLocation(SlashBlade.modid, "tips/refine");

    static private final ResourceLocation STARTINGGRIND = new ResourceLocation(SlashBlade.modid, "tips/gettingstarted");

    static private final ResourceLocation SHARPED = new ResourceLocation(SlashBlade.modid, "tips/sharped");

    static private final ResourceLocation OVERSHARP = new ResourceLocation(SlashBlade.modid, "tips/unlockedpotential");

    static private final ResourceLocation WHYTHEFUCKWOULDYOUWANTTOUNLOCKTHISACHIEVEMENT = new ResourceLocation(SlashBlade.modid, "tips/final");

    static private final TagKey<Item> soul = ItemTags.create(new ResourceLocation("slashblade","proudsouls"));

    @SubscribeEvent
    public void onAnvilRepairEvent(AnvilRepairEvent event){

        if(!(event.getEntity() instanceof ServerPlayer)) return;

        ItemStack material = event.getRight();//.getIngredientInput();
        ItemStack base = event.getLeft();//.getItemInput();
        ItemStack output = event.getOutput();

        if(base.isEmpty()) return;
        if(!(base.getItem() instanceof ItemSlashBlade)) return;
        if(material.isEmpty()) return;

        boolean isRepairable = base.getItem().isValidRepairItem(base,material);

        if (!isRepairable) {
            //Including the nether star
            if (!material.is(Items.NETHER_STAR)) {
                return;
            }
        }

        int before = base.getCapability(ItemSlashBlade.BLADESTATE).map(s->s.getRefine()).orElse(0);
        int after = output.getCapability(ItemSlashBlade.BLADESTATE).map(s->s.getRefine()).orElse(0);

        if(before < after)
            AdvancementHelper.grantCriterion((ServerPlayer) event.getEntity(), REFINE);

        if (after >= 10) {
            AdvancementHelper.grantCriterion((ServerPlayer) event.getEntity(), STARTINGGRIND);
        }

        if (after >= 100) {
            AdvancementHelper.grantCriterion((ServerPlayer) event.getEntity(), SHARPED);
        }

        if (after >= 500) {
            AdvancementHelper.grantCriterion((ServerPlayer) event.getEntity(), WHYTHEFUCKWOULDYOUWANTTOUNLOCKTHISACHIEVEMENT);
        }

        if (after >= 501) {
            AdvancementHelper.grantCriterion((ServerPlayer) event.getEntity(), OVERSHARP);
        }
    }

}
