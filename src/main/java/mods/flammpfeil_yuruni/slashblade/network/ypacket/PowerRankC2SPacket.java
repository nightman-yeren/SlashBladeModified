package mods.flammpfeil_yuruni.slashblade.network.ypacket;

import mods.flammpfeil_yuruni.slashblade.capability.powerrank.BladeChargeProvider;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PowerRankC2SPacket {

    public PowerRankC2SPacket() {

    }

    public PowerRankC2SPacket(FriendlyByteBuf buf) {

    }

    public void toBytes(FriendlyByteBuf buf) {

    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            assert player != null;
            ServerLevel level = player.serverLevel().getLevel();

            player.getCapability(BladeChargeProvider.BLADE_CHARGE).ifPresent(charge -> {
                charge.subCharges(1);
                player.sendSystemMessage(Component.literal("Current Charge: " + charge.getPowerCharges()).withStyle(ChatFormatting.AQUA));
            });
        });
        return true;
    }
}
