package mods.flammpfeil_yuruni.slashblade.capability.powerrank;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BladeChargeProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

    public static Capability<BladeCharge> BLADE_CHARGE = CapabilityManager.get(new CapabilityToken<BladeCharge>(){});

    private BladeCharge charge = null;
    private final LazyOptional<BladeCharge> optional = LazyOptional.of(this::createPlayerPowerCharge);

    private BladeCharge createPlayerPowerCharge() {
        if (this.charge == null) {
            this.charge = new BladeCharge();
        }

        return this.charge;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == BLADE_CHARGE) {
            return optional.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        createPlayerPowerCharge().saveNBT(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createPlayerPowerCharge().loadNBT(nbt);
    }
}
