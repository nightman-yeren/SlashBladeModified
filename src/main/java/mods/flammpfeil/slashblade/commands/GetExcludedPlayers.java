package mods.flammpfeil.slashblade.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import mods.flammpfeil.slashblade.util.ExcludedPlayers;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;

public class GetExcludedPlayers {

    public GetExcludedPlayers(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("slashBlade")
        .requires(cs->cs.hasPermission(2))
        .then(Commands.literal("excludePlayer")
        .then(Commands.literal("get")
        .executes(this::getPlayers))));
    }

    private int getPlayers(CommandContext<CommandSourceStack> ctx) {
        CommandSourceStack source = ctx.getSource();
        ServerPlayer player = source.getPlayer();
        assert player != null;
        List<String> players = ExcludedPlayers.getPlayers(ctx);
        boolean isEmpty = players.isEmpty();
        if (!isEmpty) {
            source.sendSuccess(() -> Component.literal("Player(s) excluded from locking:"), true);
            for (int i = 0; i < players.size(); i++) {
                int finalI = i;
                source.sendSuccess(() -> Component.literal(String.valueOf(finalI + 1) + ", " + players.get(finalI)), true);
            }
            return 1;
        } else {
            source.sendFailure(Component.literal("No players found in exclude list"));
            return 0;
        }
    }

}
