package mods.flammpfeil_yuruni.slashblade.capability.powerrank;

import net.minecraft.nbt.CompoundTag;

public class BladeCharge {

    private int bladeCharge;
    private final int MAX_CHARGES = 10;
    private final int MIN_CHARGES = 0;

    public int getPowerCharges() {
        return bladeCharge;
    }

    public void addCharges(int charges) {
        this.bladeCharge = Math.min(bladeCharge + charges, MAX_CHARGES);
    }

    public void subCharges(int charges) {
        this.bladeCharge = Math.max(bladeCharge - charges, MIN_CHARGES);
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
