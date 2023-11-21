package mods.flammpfeil.slashblade.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import mods.flammpfeil.slashblade.SlashBlade;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class HitRule {

    public HitRule(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("slashBlade")
                .requires(cs->cs.hasPermission(2))
                .then(Commands.literal("hitRule")
                .then(Commands.literal("attackPlayers")
                .then(Commands.argument("value", BoolArgumentType.bool())
                .executes(command -> {
                    return changeAttackPlayerRule(command, BoolArgumentType.getBool(command, "value"));
                })))));
        dispatcher.register(Commands.literal("slashBlade")
                .requires(cs->cs.hasPermission(2))
                .then(Commands.literal("hitRule")
                .then(Commands.literal("attackPassive")
                .then(Commands.argument("value", BoolArgumentType.bool())
                .executes(command -> {
                    return changeAttackPassiveRule(command, BoolArgumentType.getBool(command, "value"));
                })))));
        dispatcher.register(Commands.literal("slashBlade")
                .requires(cs->cs.hasPermission(2))
                .then(Commands.literal("hitRule")
                .then(Commands.literal("attackAggressive")
                .then(Commands.argument("value", BoolArgumentType.bool())
                .executes(command -> {
                    return changeAttackAggressiveRule(command, BoolArgumentType.getBool(command, "value"));
                })))));
    }

    private int changeAttackPlayerRule(CommandContext<CommandSourceStack> ctx, Boolean value) throws CommandSyntaxException {
        CommandSourceStack source = ctx.getSource();
        ServerPlayer player = source.getPlayer();
        LocalPlayer lPlayer = Minecraft.getInstance().player;
        assert player != null;
        assert lPlayer != null;
        boolean prevVal = player.getPersistentData().getBoolean(SlashBlade.modid + "hitPlayer");
        player.getPersistentData().putBoolean(SlashBlade.modid + "hitPlayer", value);
        lPlayer.getPersistentData().putBoolean(SlashBlade.modid + "hitPlayer", value);
        source.sendSuccess(() -> Component.literal("(hitPlayer) changed to " + value + " ! (Previous is [" + prevVal + "])"), true);
        return 1;
    }

    private int changeAttackPassiveRule(CommandContext<CommandSourceStack> ctx, Boolean value) throws CommandSyntaxException {
        CommandSourceStack source = ctx.getSource();
        ServerPlayer player = source.getPlayer();
        LocalPlayer lPlayer = Minecraft.getInstance().player;
        assert player != null;
        assert lPlayer != null;
        boolean prevVal = player.getPersistentData().getBoolean(SlashBlade.modid + "hitPassive");
        player.getPersistentData().putBoolean(SlashBlade.modid + "hitPassive", value);
        lPlayer.getPersistentData().putBoolean(SlashBlade.modid + "hitPassive", value);
        source.sendSuccess(() -> Component.literal("(hitPassive) changed to " + value + " ! (Previous is [" + prevVal + "])"), true);
        return 1;
    }

    private int changeAttackAggressiveRule(CommandContext<CommandSourceStack> ctx, Boolean value) throws CommandSyntaxException {
        CommandSourceStack source = ctx.getSource();
        ServerPlayer player = source.getPlayer();
        LocalPlayer lPlayer = Minecraft.getInstance().player;
        assert player != null;
        assert lPlayer != null;
        boolean prevVal = player.getPersistentData().getBoolean(SlashBlade.modid + "hitAggressive");
        player.getPersistentData().putBoolean(SlashBlade.modid + "hitAggressive", value);
        lPlayer.getPersistentData().putBoolean(SlashBlade.modid + "hitAggressive", value);
        source.sendSuccess(() -> Component.literal("(hitAggressive) changed to " + value + " ! (Previous is [" + prevVal + "])"), true);
        return 1;
    }

}
