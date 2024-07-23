package mods.flammpfeil_yuruni.slashblade.network.ypacket;

import mods.flammpfeil_yuruni.slashblade.capability.bladecharge.BladeChargeProvider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class BladeChargeSubtractC2SPacket {

    public BladeChargeSubtractC2SPacket() {

    }

    public BladeChargeSubtractC2SPacket(FriendlyByteBuf buf) {

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
                charge.subCharges(1, player);
            });
        });
        return true;
    }
}
