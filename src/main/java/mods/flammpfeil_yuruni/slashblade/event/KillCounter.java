package mods.flammpfeil_yuruni.slashblade.event;

import mods.flammpfeil_yuruni.slashblade.init.SBItems;
import mods.flammpfeil_yuruni.slashblade.item.ItemSlashBlade;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class KillCounter {
    private static final class SingletonHolder {
        private static final KillCounter instance = new KillCounter();
    }
    public static KillCounter getInstance() {
        return SingletonHolder.instance;
    }
    private KillCounter(){}
    public void register(){
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onLivingDeathEvent(LivingDeathEvent event) {
        Entity trueSource = event.getSource().getEntity();

        if (!(trueSource instanceof LivingEntity)) return;

        ItemStack stack = ((LivingEntity) trueSource).getMainHandItem();
        if(stack.isEmpty()) return;
        if(!(stack.getItem() instanceof ItemSlashBlade)) return;

        stack.getCapability(ItemSlashBlade.BLADESTATE).ifPresent(state->{
            state.setKillCount(state.getKillCount() + 1);
        });
        if (event.getEntity() instanceof Monster && Math.round(Math.random() * (5 - Math.round((float) stack.getEnchantmentLevel(Enchantments.MOB_LOOTING) / 2))) == 1) {
            ((Player) trueSource).addItem(new ItemStack(SBItems.proudsoul_tiny, stack.getEnchantmentLevel(Enchantments.MOB_LOOTING) <= 0 ? 0 : Math.round((float)stack.getEnchantmentLevel(Enchantments.MOB_LOOTING) / 2)));
        }
    }
}
