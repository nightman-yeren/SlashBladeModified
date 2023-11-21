package mods.flammpfeil.slashblade.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.sun.jdi.connect.Connector;
import mods.flammpfeil.slashblade.SlashBlade;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class ExcludePlayerLock {

    public ExcludePlayerLock(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("slashBlade")
                .requires(cs->cs.hasPermission(2))
                .then(Commands.literal("excludePlayer")
                .then(Commands.literal("add")
                .then(Commands.argument("playerName", StringArgumentType.string())
                .executes(command -> {
                    return addPlayer(command, StringArgumentType.getString(command, "playerName"));
                })))));
    }

    private int addPlayer(CommandContext<CommandSourceStack> ctx, String name) throws CommandSyntaxException {
        CommandSourceStack source = ctx.getSource();
        ServerPlayer player = source.getPlayer();
        assert player != null;
        player.getPersistentData().putString(SlashBlade.modid + "excludedPlayer-" + name, name);
        source.sendSuccess(() -> Component.literal("(" + name + ") added to excluded players list!"), true);
        return 1;
    }
}
