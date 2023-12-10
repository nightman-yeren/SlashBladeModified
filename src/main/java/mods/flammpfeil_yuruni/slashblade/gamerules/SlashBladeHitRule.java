package mods.flammpfeil_yuruni.slashblade.gamerules;

import mods.flammpfeil_yuruni.slashblade.SlashBlade;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;

public class SlashBladeHitRule extends GameRules {

    public static Key<BooleanValue> SLASHBLADE_HITPASSIVE = null;
    public static Key<BooleanValue> SLASHBLADE_HITPLAYER = null;
    public static Key<BooleanValue> SLASHBLADE_SELECTOR_DEFAULT_TRUE = null;

    public static void init() {
        SLASHBLADE_HITPASSIVE = GameRules.register("sBladeHitPassive", GameRules.Category.PLAYER, GameRules.BooleanValue.create(true));
        SLASHBLADE_HITPLAYER = GameRules.register("sBladeHitPlayer", GameRules.Category.PLAYER, GameRules.BooleanValue.create(true));
        SLASHBLADE_SELECTOR_DEFAULT_TRUE = GameRules.register("sBladeSelectorDefaultTrue", GameRules.Category.PLAYER, GameRules.BooleanValue.create(false));
    }

    public static boolean isEnabled(LevelAccessor world, Key<BooleanValue> key) {
        if (!(world instanceof Level)) {
            return false;
        }
        if (SlashBlade.serverMemory.getCurrentServer() == null) {
            return ((Level) world).getGameRules().getRule(key).get();
        }
        return SlashBlade.serverMemory.getCurrentServer().getGameRules().getBoolean(key);
        //Code I can borrow if something went wrong
        /*
        Minecraft mc = Minecraft.getInstance();
        if (mc.isSingleplayer()) {
            return mc.getSingleplayerServer().getGameRules().getRule(key).get();
        } else {
            return ((Level) world).getGameRules().getBoolean(key);
        }
         */
    }

}
