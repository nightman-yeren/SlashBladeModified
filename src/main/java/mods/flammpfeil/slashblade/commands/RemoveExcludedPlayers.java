package mods.flammpfeil.slashblade.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import mods.flammpfeil.slashblade.SlashBlade;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class RemoveExcludedPlayers {
    public RemoveExcludedPlayers(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("slashBlade")
            .requires(cs->cs.hasPermission(2))
            .then(Commands.literal("excludePlayer")
            .then(Commands.literal("remove")
            .then(Commands.argument("playerName", StringArgumentType.string())
            .executes((command) -> {
                return removePlayer(command, StringArgumentType.getString(command, "playerName"));
            })))));
    }

    private int removePlayer(CommandContext<CommandSourceStack> ctx, String name) throws CommandSyntaxException {
        CommandSourceStack source = ctx.getSource();
        ServerPlayer player = source.getPlayer();
        assert player != null;
        boolean isNameInData = !player.getPersistentData().getString(SlashBlade.modid + "excludedPlayer-" + name).isEmpty();
        if (isNameInData) {
            player.getPersistentData().remove(SlashBlade.modid + "excludedPlayer-" + name);
            source.sendSuccess(() -> Component.literal("(" + name + ") removed from excluded players list!"), true);
            return 1;
        } else {
            source.sendFailure(Component.literal("Player not found!"));
            return -1;
        }
    }
}
