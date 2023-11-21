package mods.flammpfeil.slashblade.client.renderer.model;

import com.mojang.blaze3d.vertex.PoseStack;
import mods.flammpfeil.slashblade.client.renderer.layers.LayerMainBlade;
import mods.flammpfeil.slashblade.client.renderer.util.MSAutoCloser;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.CameraType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionHand;
import com.mojang.math.Axis;

/**
 * Created by Furia on 2016/02/07.
 */
public class BladeFirstPersonRender {
    private LayerMainBlade layer = null;
    private BladeFirstPersonRender(){
        Minecraft mc = Minecraft.getInstance();

        EntityRenderer renderer = mc.getEntityRenderDispatcher().getRenderer(mc.player);
        if(renderer instanceof RenderLayerParent)
            layer = new LayerMainBlade((RenderLayerParent)renderer);
    }
    private static final class SingletonHolder {
        private static final BladeFirstPersonRender instance = new BladeFirstPersonRender();
    }
    public static BladeFirstPersonRender getInstance(){
        return SingletonHolder.instance;
    }

    public void render(PoseStack matrixStack, MultiBufferSource bufferIn, int combinedLightIn){
        if(layer == null)
            return;
        
        Minecraft mc = Minecraft.getInstance();
        boolean flag = mc.getCameraEntity() instanceof LivingEntity && ((LivingEntity) mc.getCameraEntity()).isSleeping();
        if (!(mc.options.getCameraType() == CameraType.FIRST_PERSON && !flag && !mc.options.hideGui && !mc.gameMode.isAlwaysFlying())) {
            return;
        }
        LocalPlayer player = mc.player;
        ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
        if (stack.isEmpty()) return;
        if (!(stack.getItem() instanceof ItemSlashBlade)) return;

        try(MSAutoCloser msac = MSAutoCloser.pushMatrix(matrixStack)){
            PoseStack.Pose me = matrixStack.last();
            me.pose().identity();
            me.normal().identity();

            matrixStack.translate(0.0f, 0.0f, -0.5f);
            matrixStack.mulPose(Axis.ZP.rotationDegrees(180.0f));
            matrixStack.scale(1.2F, 1.0F, 1.0F);

            //no sync pitch
            matrixStack.mulPose(Axis.XP.rotationDegrees(-mc.player.getXRot()));

            //layer.disableOffhandRendering();
            float partialTicks = mc.getFrameTime();
            layer.render(matrixStack, bufferIn, combinedLightIn, mc.player, 0, 0, partialTicks, 0, 0, 0);
        }
    }
}