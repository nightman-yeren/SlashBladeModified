package mods.flammpfeil_yuruni.slashblade.enchantment;

import mods.flammpfeil_yuruni.slashblade.capability.bladecharge.BladeChargeProvider;
import mods.flammpfeil_yuruni.slashblade.item.ItemSlashBlade;
import mods.flammpfeil_yuruni.slashblade.util.MathTeacher;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import org.jetbrains.annotations.NotNull;

public class ChargeStealerEnchantment extends Enchantment { //TODO: Add way to get this enchantment
    public ChargeStealerEnchantment(Rarity rarity, EnchantmentCategory Category, EquipmentSlot... ApplicableSlots) {
        super(rarity, Category, ApplicableSlots);
    }

    @Override
    public boolean canEnchant(ItemStack itemStack) {
        return itemStack.getItem() instanceof ItemSlashBlade;
    }

    @Override
    public void doPostAttack(@NotNull LivingEntity attacker, @NotNull Entity target, int enchantLevel) { //TODO: particles
        if (!attacker.level().isClientSide) {
            if (!(attacker instanceof Player)) return;
            ServerPlayer source = ((ServerPlayer) attacker);

            if (enchantLevel > 0) {
                float percentage = (float) (enchantLevel * 20) / 100; //Blade charge addition chance calculation(Do percentage then convert to decimals) (level 2 would be 40 / 100 = 0.4)
                float randomChance = (float) MathTeacher.advancedRound(Math.random(), 1); //Round random to tenth
                if (randomChance <= percentage) {
                    attacker.getCapability(BladeChargeProvider.BLADE_CHARGE).ifPresent(bladeCharge -> {
                        bladeCharge.addCharges(1, source);
                    });
                }
            }
        }
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }
}
