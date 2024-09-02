package mods.flammpfeil_yuruni.slashblade.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import mods.flammpfeil_yuruni.slashblade.config.ClientConfigs;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class BladeChargeHudSetCommand {

    public BladeChargeHudSetCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("slashBlade")
                .then(Commands.literal("bladeChargeHud")
                        .executes(this::sayHudAxis)
                        .then(Commands.literal("setXOffset")
                            .then(Commands.argument("xOffset", IntegerArgumentType.integer())
                                    .executes(commandContext -> {
                                        return setXOffset(commandContext, IntegerArgumentType.getInteger(commandContext, "xOffset"));
                                    })))
                        .then(Commands.literal("setYOffset")
                            .then(Commands.argument("yOffset", IntegerArgumentType.integer())
                                    .executes(commandContext -> {
                                        return setYOffset(commandContext, IntegerArgumentType.getInteger(commandContext, "yOffset"));
                                    })))
                        .then(Commands.literal("reset")
                            .executes(this::resetOffsets))
                ));
    }

    private int sayHudAxis(CommandContext<CommandSourceStack> ctx) {
        CommandSourceStack source = ctx.getSource();
        Component overlayXComponent = Component.literal("Overlay X Offset: " + ClientConfigs.BLADECHARGE_OVERLAY_OFFSET_X.get());
        Component overlayYComponent = Component.literal("Overlay Y Offset: " + ClientConfigs.BLADECHARGE_OVERLAY_OFFSET_Y.get());
        Component newLineComponent = Component.literal("\n");
        Component finalComponent = overlayXComponent.copy().append(newLineComponent).append(overlayYComponent);
        source.sendSuccess(() -> finalComponent, true);
        return 1;
    }

    private int setXOffset(CommandContext<CommandSourceStack> ctx, int xOffset) {
        CommandSourceStack source = ctx.getSource();
        ClientConfigs.BLADECHARGE_OVERLAY_OFFSET_X.set(xOffset);
        Component textComponent = Component.literal("Set X Offset to " + xOffset);
        source.sendSuccess(() -> textComponent, true);
        return 1;
    }

    private int setYOffset(CommandContext<CommandSourceStack> ctx, int yOffset) {
        CommandSourceStack source = ctx.getSource();
        ClientConfigs.BLADECHARGE_OVERLAY_OFFSET_Y.set(yOffset);
        Component textComponent = Component.literal("Set Y Offset to " + yOffset);
        source.sendSuccess(() -> textComponent, true);
        return 1;
    }

    private int resetOffsets(CommandContext<CommandSourceStack> ctx) {
        CommandSourceStack source = ctx.getSource();
        ClientConfigs.BLADECHARGE_OVERLAY_OFFSET_X.set(ClientConfigs.BLADECHARGE_OVERLAY_OFFSET_X.getDefault());
        ClientConfigs.BLADECHARGE_OVERLAY_OFFSET_Y.set(ClientConfigs.BLADECHARGE_OVERLAY_OFFSET_Y.getDefault());
        source.sendSuccess(() -> Component.literal("Done reset overlay position"), true);
        return 1;
    }

}
