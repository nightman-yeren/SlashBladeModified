package mods.flammpfeil_yuruni.slashblade.gamerules;

import mods.flammpfeil_yuruni.slashblade.SlashBlade;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.fml.loading.targets.FMLServerLaunchHandler;

import java.util.Objects;

public class SlashBladeHitRule extends GameRules {

    public static Key<BooleanValue> SLASHBLADE_HITPASSIVE = null;
    public static Key<BooleanValue> SLASHBLADE_HITPLAYER = null;

    public static void init() {
        SLASHBLADE_HITPASSIVE = GameRules.register("sBladeHitPassive", GameRules.Category.PLAYER, GameRules.BooleanValue.create(true));
        SLASHBLADE_HITPLAYER = GameRules.register("sBladeHitPlayer", GameRules.Category.PLAYER, GameRules.BooleanValue.create(true));;
    }

    public static boolean isEnabled(Key<BooleanValue> key) {
        Minecraft mc = Minecraft.getInstance();
        //if (mc.isSingleplayer()) {
            //return mc.getSingleplayerServer().getGameRules().getRule(key).get();
        //} else {

        //}
        //return world.getGameRules().getBoolean(key);
        //^^^ Code I could borrow if something went wrong
        return SlashBlade.serverMemory.getCurrentServer().getGameRules().getBoolean(key);
    }

    public static boolean isEnabled(LevelAccessor world, Key<BooleanValue> key) {
        if (!(world instanceof Level)) {
            return false;
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
