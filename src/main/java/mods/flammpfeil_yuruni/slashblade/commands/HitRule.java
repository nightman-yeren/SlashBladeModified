package mods.flammpfeil_yuruni.slashblade.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import mods.flammpfeil_yuruni.slashblade.SlashBlade;
import mods.flammpfeil_yuruni.slashblade.util.FileUtils;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

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
        boolean prevVal = SlashBlade.hitRuleMemory.isHitRulePlayer();
        if (prevVal == value) {
            source.sendSuccess(() -> Component.literal("(hitPlayer) value same, no changes made"), true);
            return 0;
        }
        SlashBlade.hitRuleMemory.setHitRulePlayer(value);
        FileUtils.saveDataToFile("hitRulePlayer", value);
        source.sendSuccess(() -> Component.literal("(hitPlayer) changed to " + value + "! (Previous is [" + prevVal + "])"), true);
        return 1;
    }

    private int changeAttackPassiveRule(CommandContext<CommandSourceStack> ctx, Boolean value) throws CommandSyntaxException {
        CommandSourceStack source = ctx.getSource();
        boolean prevVal = SlashBlade.hitRuleMemory.isHitRulePassive();
        if (prevVal == value) {
            source.sendSuccess(() -> Component.literal("(hitPassive) value same, no changes made"), true);
            return 0;
        }
        SlashBlade.hitRuleMemory.setHitRulePassive(value);
        FileUtils.saveDataToFile("hitRulePassive", value);
        source.sendSuccess(() -> Component.literal("(hitPassive) changed to " + value + "! (Previous is [" + prevVal + "])"), true);
        return 1;
    }

    private int changeAttackAggressiveRule(CommandContext<CommandSourceStack> ctx, Boolean value) throws CommandSyntaxException {
        CommandSourceStack source = ctx.getSource();
        boolean prevVal = SlashBlade.hitRuleMemory.isHitRuleAggressive();
        if (prevVal == value) {
            source.sendSuccess(() -> Component.literal("(hitAggressive) value same, no changes made"), true);
            return 0;
        }
        SlashBlade.hitRuleMemory.setHitRuleAggressive(value);
        FileUtils.saveDataToFile("hitRuleAggressive", value);
        source.sendSuccess(() -> Component.literal("(hitAggressive) changed to " + value + "! (Previous is [" + prevVal + "])"), true);
        return 1;
    }

}
