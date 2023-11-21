package mods.flammpfeil.slashblade.network;

import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.capability.concentrationrank.CapabilityConcentrationRank;
import mods.flammpfeil.slashblade.capability.concentrationrank.IConcentrationRank;
import mods.flammpfeil.slashblade.capability.slashblade.ComboState;
import mods.flammpfeil.slashblade.event.BladeMotionEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class MotionBroadcastMessage {
    public UUID playerId;
    public String combo;



    public MotionBroadcastMessage(){}

    static public MotionBroadcastMessage decode(FriendlyByteBuf buf) {
        MotionBroadcastMessage msg = new MotionBroadcastMessage();
        msg.playerId = buf.readUUID();
        msg.combo = buf.readUtf();
        return msg;
    }

    static public void encode(MotionBroadcastMessage msg, FriendlyByteBuf buf) {
        buf.writeUUID(msg.playerId);
        buf.writeUtf(msg.combo);
    }

    static public void handle(MotionBroadcastMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().setPacketHandled(true);

        if(ctx.get().getDirection() != NetworkDirection.PLAY_TO_CLIENT) {
            return;
        }

        BiConsumer<UUID,String> handler = DistExecutor.callWhenOn(Dist.CLIENT, ()->()-> MotionBroadcastMessage::setPoint);

        if(handler != null)
            ctx.get().enqueueWork(() -> {
                handler.accept(msg.playerId, msg.combo);
            });

    }

    @OnlyIn(Dist.CLIENT)
    static public void setPoint(UUID playerId, String combo){
        Player target = Minecraft.getInstance().level.getPlayerByUUID(playerId);

        if(target == null) return;
        if(!(target instanceof AbstractClientPlayer)) return;

        ComboState state = ComboState.NONE.valueOf(combo);
        if(state == null) return;

        MinecraftForge.EVENT_BUS.post(new BladeMotionEvent(target, state));
    }
}