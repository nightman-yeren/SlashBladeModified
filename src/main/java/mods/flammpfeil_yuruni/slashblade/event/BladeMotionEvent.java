package mods.flammpfeil_yuruni.slashblade.event;

import mods.flammpfeil_yuruni.slashblade.capability.slashblade.ComboState;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.eventbus.api.Event;

public class BladeMotionEvent extends Event{
    private final LivingEntity entity;
    private final ComboState combo;

    public BladeMotionEvent(LivingEntity entity, ComboState combo)
    {
        this.entity = entity;
        this.combo = combo;
    }

    public LivingEntity getEntity()
    {
        return entity;
    }

    public ComboState getCombo(){
        return this.combo;
    }
}
