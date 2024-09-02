package mods.flammpfeil_yuruni.slashblade.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfigs {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Integer> BLADECHARGE_OVERLAY_OFFSET_X;
    public static final ForgeConfigSpec.ConfigValue<Integer> BLADECHARGE_OVERLAY_OFFSET_Y;

    static {
        BUILDER.push("Configs for SlashBlade");
        //Configs
        BLADECHARGE_OVERLAY_OFFSET_X = BUILDER.comment("The X axis offset of the blade charge overlay")
                .define("BladeCharge Hud X Offset", 50);
        BLADECHARGE_OVERLAY_OFFSET_Y = BUILDER.comment("The Y axis offset of the blade charge overlay")
                .define("BladeCharge Hud Y Offset", 54);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}