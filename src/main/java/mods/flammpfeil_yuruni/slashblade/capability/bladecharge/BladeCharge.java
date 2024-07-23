package mods.flammpfeil_yuruni.slashblade.capability.bladecharge;

import mods.flammpfeil_yuruni.slashblade.SlashBlade;
import mods.flammpfeil_yuruni.slashblade.network.YMessages;
import mods.flammpfeil_yuruni.slashblade.network.ypacket.BladeChargeSyncS2CPacket;
import mods.flammpfeil_yuruni.slashblade.util.AdvancementHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class BladeCharge {

    private int bladeCharge;
    private final int MAX_CHARGES = 10;
    private final int MIN_CHARGES = 0;

    public int getPowerCharges() {
        return bladeCharge;
    }

    public void addCharges(int charges, ServerPlayer player) {
        this.bladeCharge = Math.min(bladeCharge + charges, MAX_CHARGES);
        YMessages.sendToPlayer(new BladeChargeSyncS2CPacket(this.bladeCharge), player);
        AdvancementHelper.grantCriterion(player, new ResourceLocation(SlashBlade.modid, "tips/bladecharge"));
    }

    public void subCharges(int charges, ServerPlayer player) {
        this.bladeCharge = Math.max(bladeCharge - charges, MIN_CHARGES);
        YMessages.sendToPlayer(new BladeChargeSyncS2CPacket(this.bladeCharge), player);
        AdvancementHelper.grantCriterion(player, new ResourceLocation(SlashBlade.modid, "tips/bladecharge"));
    }

    public void copyFrom(BladeCharge source) {
        this.bladeCharge = source.bladeCharge;
    }

    public void saveNBT(CompoundTag nbt) {
        nbt.putInt("bladeCharges", bladeCharge);
    }

    public void loadNBT(CompoundTag nbt) {
        bladeCharge = nbt.getInt("bladeCharges");
    }

}
