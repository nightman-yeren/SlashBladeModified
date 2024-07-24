package mods.flammpfeil_yuruni.slashblade.network;

import mods.flammpfeil_yuruni.slashblade.SlashBlade;
import mods.flammpfeil_yuruni.slashblade.network.ypacket.BladeChargeSubtractC2SPacket;
import mods.flammpfeil_yuruni.slashblade.network.ypacket.BladeChargeSyncS2CPacket;
import mods.flammpfeil_yuruni.slashblade.network.ypacket.CanceledSkillSyncS2CPacket;
import net.minecraft.client.Minecraft;
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

        net.messageBuilder(BladeChargeSubtractC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(BladeChargeSubtractC2SPacket::new)
                .encoder(BladeChargeSubtractC2SPacket::toBytes)
                .consumerMainThread(BladeChargeSubtractC2SPacket::handle)
                .add();

        net.messageBuilder(BladeChargeSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(BladeChargeSyncS2CPacket::new)
                .encoder(BladeChargeSyncS2CPacket::toBytes)
                .consumerMainThread(BladeChargeSyncS2CPacket::handle)
                .add();

        net.messageBuilder(CanceledSkillSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(CanceledSkillSyncS2CPacket::new)
                .encoder(CanceledSkillSyncS2CPacket::toBytes)
                .consumerMainThread(CanceledSkillSyncS2CPacket::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message) {
        if (Minecraft.getInstance().getConnection() == null) return;
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

    public static <MSG> void sendToAllPlayers(MSG msg) {
        INSTANCE.send(PacketDistributor.ALL.noArg(), msg);
    }

}
