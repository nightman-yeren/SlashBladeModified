package mods.flammpfeil_yuruni.slashblade.network;

import mods.flammpfeil_yuruni.slashblade.SlashBlade;
import mods.flammpfeil_yuruni.slashblade.network.ypacket.PowerRankC2SPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class YMessages {

    private static SimpleChannel INSTANCE;

    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }

    public static void register() {

        SimpleChannel net = NetworkRegistry.newSimpleChannel(new ResourceLocation(SlashBlade.modid, "messages"),
                () -> "1.0",
                s -> true,
                s -> true
        );

        INSTANCE = net;

        net.messageBuilder(PowerRankC2SPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(PowerRankC2SPacket::new)
                .encoder(PowerRankC2SPacket::toBytes)
                .consumerMainThread(PowerRankC2SPacket::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

}
