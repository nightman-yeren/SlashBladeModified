package mods.flammpfeil_yuruni.slashblade.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import dev.kosmx.playerAnim.api.layered.AnimationStack;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import mods.flammpfeil_yuruni.slashblade.optional.playerAnim.PlayerAnimationOverrider;
import mods.flammpfeil_yuruni.slashblade.optional.playerAnim.VmdAnimation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class TestAnimationCommand {

    public TestAnimationCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("slashBlade")
        .then(Commands.literal("executeAnimation")
                .then(Commands.argument("animationID", StringArgumentType.string())
                        .executes(context -> {
                            return getAnimation(context, StringArgumentType.getString(context, "animationID"));
                        }))));
    }

    private void sendAnimationMessage(CommandSourceStack source, String animationName) {
        source.sendSuccess(() -> Component.literal("Executing animation [" + animationName + "]"), true);
    }

    @SuppressWarnings("Unchecked")
    private void tryExecuteAnimation(Player player, CommandSourceStack source, String animationName) {
        try {
            /*
            ModifierLayer<IAnimation> animation = (ModifierLayer<IAnimation>) PlayerAnimationAccess.getPlayerAssociatedData((AbstractClientPlayer) player).get(new ResourceLocation(modid, "player_animation"));
            if (animation != null) {
                animation.setAnimation(new KeyframeAnimationPlayer(PlayerAnimationRegistry.getAnimation(new ResourceLocation("slashblade",  "player_animation/" + animationName + ".json"))));
            }
            */
            PlayerAnimationOverrider VMDAnimationInstance = PlayerAnimationOverrider.getInstance();
            AnimationStack animationStack = PlayerAnimationAccess.getPlayerAnimLayer((AbstractClientPlayer) player);
            VmdAnimation vmdAnimation = VMDAnimationInstance.animation.get(animationName);
            if(vmdAnimation != null) {
                animationStack.removeLayer(0);
                vmdAnimation.play();
                animationStack.addAnimLayer(0, vmdAnimation);
                sendAnimationMessage(source, animationName);
            } else {
                source.sendFailure(Component.literal("Animation Not Found"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            source.sendFailure(Component.literal("Failed to execute animation, printing stack trace"));
        }
    }

    private int getAnimation(CommandContext<CommandSourceStack> ctx, String ID) {
        CommandSourceStack source = ctx.getSource();
        ServerPlayer serverPlayer = source.getPlayer();
        assert Minecraft.getInstance().level != null;
        assert serverPlayer != null;
        Player player = Minecraft.getInstance().level.getPlayerByUUID(serverPlayer.getUUID());
        assert player != null;
        tryExecuteAnimation(player, source, ID);
        return 1;
    }

}
