package mods.flammpfeil_yuruni.slashblade.entity.ai;

import mods.flammpfeil_yuruni.slashblade.capability.mobeffect.CapabilityMobEffect;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class StunGoal extends Goal {
    private final PathfinderMob entity;

    public StunGoal(PathfinderMob creature) {
        this.entity = creature;
        this.setFlags(EnumSet.of(Flag.MOVE,Flag.JUMP,Flag.LOOK,Flag.TARGET));
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean canUse() {
        boolean onStun = this.entity.getCapability(CapabilityMobEffect.MOB_EFFECT)
                .filter((state)->state.isStun(this.entity.level().getGameTime()))
                .isPresent();

        return onStun;
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    public void stop() {
        this.entity.getCapability(CapabilityMobEffect.MOB_EFFECT)
                .ifPresent((state)->{
                    state.clearStunTimeOut();
                });
    }
}
