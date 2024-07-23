package mods.flammpfeil_yuruni.slashblade.client.renderer.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import mods.flammpfeil_yuruni.slashblade.SlashBlade;
import mods.flammpfeil_yuruni.slashblade.client.data.BladeChargeData;
import mods.flammpfeil_yuruni.slashblade.item.ItemSlashBlade;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class BladeChargeOverlay {

    private static final ResourceLocation FILLED_CHARGE = new ResourceLocation(SlashBlade.modid, "textures/gui/bladecharged.png");
    private static final ResourceLocation EMPTY_CHARGE = new ResourceLocation(SlashBlade.modid, "textures/gui/bladecharge.png");

    public static final IGuiOverlay HUD_CHARGE = ((gui, guiGraphics, partialTick, screenWidth, screenHeight) -> {
        Player player = Minecraft.getInstance().player;
        if (player == null) return;
        ItemStack mainHandStack = player.getMainHandItem();

        if (!(mainHandStack.getItem() instanceof ItemSlashBlade)) return;

        int x = screenWidth / 2;
        int y = screenHeight;

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, EMPTY_CHARGE);

        for (int i = 0; i < 10; i++) {
            guiGraphics.blit(EMPTY_CHARGE, x - 94 + (i * 9), y - 54, 0, 0, 12, 12, 12, 12);
        }

        RenderSystem.setShaderTexture(0, FILLED_CHARGE);
        for (int i = 0; i < 10; i++) {
            if (BladeChargeData.getCharges() > i) {
                guiGraphics.blit(FILLED_CHARGE, x - 94 + (i * 9), y - 54, 0, 0, 12, 12, 12, 12);
            } else {
                break;
            }
        }

    });

}
