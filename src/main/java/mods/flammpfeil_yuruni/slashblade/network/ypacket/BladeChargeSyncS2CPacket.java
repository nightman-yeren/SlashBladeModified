package mods.flammpfeil_yuruni.slashblade.network.ypacket;

import mods.flammpfeil_yuruni.slashblade.client.data.BladeChargeData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class BladeChargeSyncS2CPacket {

    private final int bladeCharges;

    public BladeChargeSyncS2CPacket(int charges) {
        this.bladeCharges = charges;
    }

    public BladeChargeSyncS2CPacket(FriendlyByteBuf buf) {
        this.bladeCharges = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(bladeCharges);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            BladeChargeData.set(bladeCharges);
        });
        return true;
    }
}
