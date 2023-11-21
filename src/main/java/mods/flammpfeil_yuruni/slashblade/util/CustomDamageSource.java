package mods.flammpfeil_yuruni.slashblade.util;

import mods.flammpfeil_yuruni.slashblade.SlashBlade;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;

public class CustomDamageSource {
    public static final ResourceKey<DamageType> SUMMONED_SWORD = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(SlashBlade.modid,"summonedsword"));

}
