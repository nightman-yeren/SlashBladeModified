package mods.flammpfeil_yuruni.slashblade.enchantment;

import mods.flammpfeil_yuruni.slashblade.SlashBlade;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SlashBladeEnchantments {
    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, SlashBlade.modid);

    public static RegistryObject<Enchantment> CHARGE_STEALER = ENCHANTMENTS.register("charge_stealer", () -> new ChargeStealerEnchantment(Enchantment.Rarity.RARE, EnchantmentCategory.WEAPON, EquipmentSlot.MAINHAND));

    public static void Register(IEventBus eventBus) {
        ENCHANTMENTS.register(eventBus);
    }
}
