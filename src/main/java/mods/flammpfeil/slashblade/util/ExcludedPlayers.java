package mods.flammpfeil.slashblade.util;

import com.mojang.brigadier.context.CommandContext;
import mods.flammpfeil.slashblade.SlashBlade;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;

public class ExcludedPlayers {

    public static List<String> getPlayers(CommandContext<CommandSourceStack> ctx) {
        CommandSourceStack source = ctx.getSource();
        ServerPlayer player = source.getPlayer();
        assert player != null;
        List<String> players = new ArrayList<>();
        for (String key : player.getPersistentData().getAllKeys()) {
            if (key.contains(SlashBlade.modid + "excludedPlayer-")) {
                    players.add(player.getPersistentData().getString(key));
            }
        }
        return players;
    }

    public static List<String> getPlayersFromKey(Player player) {
        List<String> players = new ArrayList<>();
        for (String key : player.getPersistentData().getAllKeys()) {
            if (key.contains(SlashBlade.modid + "excludedPlayer-")) {
                players.add(player.getPersistentData().getString(key));
            }
        }
        return players;
    }

    public static boolean isPlayerInList(Player origin, Player entity) {
        String playerName = entity.getName().toString();
        for (String name : getPlayersFromKey(origin)) {
            if (name.equals(playerName)) {
                return true;
            }
        }
        return false;
    }

}
