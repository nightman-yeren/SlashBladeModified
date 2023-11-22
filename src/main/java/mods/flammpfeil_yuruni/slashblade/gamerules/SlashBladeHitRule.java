package mods.flammpfeil_yuruni.slashblade.gamerules;

import net.minecraft.client.Minecraft;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;

public class SlashBladeHitRule extends GameRules {

    public static Key<BooleanValue> SLASHBLADE_HITPASSIVE = null;
    public static Key<BooleanValue> SLASHBLADE_HITPLAYER = null;

    public static void init() {
        SLASHBLADE_HITPASSIVE = GameRules.register("sBladeHitPassive", GameRules.Category.PLAYER, GameRules.BooleanValue.create(true));
        SLASHBLADE_HITPLAYER = GameRules.register("sBladeHitPlayer", GameRules.Category.PLAYER, GameRules.BooleanValue.create(true));;
    }

    public static boolean isEnabled(Key<BooleanValue> key) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.isSingleplayer()) {
            return mc.getSingleplayerServer().getGameRules().getRule(key).get();
        } else {
            return mc.player.getServer().getGameRules().getRule(key).get();
        }
        //return world.getGameRules().getBoolean(key);
    }

    public static boolean isEnabled(LevelAccessor world, Key<BooleanValue> key) {
        if (!(world instanceof Level)) {
            return false;
        }
        Minecraft mc = Minecraft.getInstance();
        if (mc.isSingleplayer()) {
            return mc.getSingleplayerServer().getGameRules().getRule(key).get();
        } else {
            return mc.player.getServer().getGameRules().getRule(key).get();
        }
    }

}
