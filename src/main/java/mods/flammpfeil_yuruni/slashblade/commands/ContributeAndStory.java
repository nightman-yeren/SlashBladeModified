package mods.flammpfeil_yuruni.slashblade.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import mods.flammpfeil_yuruni.slashblade.SlashBlade;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.FormattedCharSequence;
import net.minecraftforge.common.ForgeHooks;

import java.util.List;

public class ContributeAndStory {

    public ContributeAndStory(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("slashBladeProject")
            .then(Commands.literal("contribute")
            .executes(this::contribute)));
        dispatcher.register(Commands.literal("slashBladeProject")
            .then(Commands.literal("backgroundStory")
            .executes(this::story)));
    }

    private int contribute(CommandContext<CommandSourceStack> ctx) {
        CommandSourceStack source = ctx.getSource();
        Component linkComponent = ForgeHooks.newChatWithLinks("https://github.com/nightman-yeren/SlashBladeModified");
        Component textComponent = Component.literal("If you are willing to contribute, either you are a skilled coder, a texture editor or a modeler, we are looking for you!\n" +
                "We are happy to accept more people into developing to bring this mod to life, or even possibly working with the original mod developer!\n");
        Component finalComponent = textComponent.copy().withStyle(ChatFormatting.GREEN).append(linkComponent);
        source.sendSuccess(() -> finalComponent, true);
        return 1;
    }

    private int story(CommandContext<CommandSourceStack> ctx) {
        CommandSourceStack source = ctx.getSource();
        Component linkComponent = ForgeHooks.newChatWithLinks("https://en.wikipedia.org/wiki/Muramasa:_The_Demon_Blade");
        Component normalTextComponent = Component.literal("Here's the wikipedia page that have a pretty good explanation about ").withStyle(ChatFormatting.AQUA);
        Component highlightedTextComponent = Component.literal("Demon Blade").withStyle(ChatFormatting.RED);
        Component finishingTouchTextComponent = Component.literal(": \n").withStyle(ChatFormatting.AQUA);
        Component finalComponent = normalTextComponent.copy().append(highlightedTextComponent).append(finishingTouchTextComponent).append(linkComponent);
        source.sendSuccess(() -> finalComponent, true);
        return 1;
    }

}
