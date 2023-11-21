package mods.flammpfeil_yuruni.slashblade.capability.concentrationrank;

import mods.flammpfeil_yuruni.slashblade.capability.slashblade.ComboState;
import net.minecraft.world.damagesource.DamageSource;

public class ConcentrationRank implements IConcentrationRank{

    long rankpoint;
    long lastupdate;
    long lastrankrise;

    static public long UnitCapacity = 300;

    public ConcentrationRank(){
        rankpoint = 0;
        lastupdate = 0;
    }

    @Override
    public long getRawRankPoint() {
        return rankpoint;
    }

    @Override
    public void setRawRankPoint(long point) {
        this.rankpoint = point;
    }

    @Override
    public long getLastUpdate() {
        return lastupdate;
    }

    @Override
    public void setLastUpdte(long time) {
        this.lastupdate = time;
    }

    @Override
    public long getLastRankRise() {
        return this.lastrankrise;
    }

    @Override
    public void setLastRankRise(long time) {
        this.lastrankrise = time;
    }

    @Override
    public long getUnitCapacity() {
        return UnitCapacity;
    }

    @Override
    public float getRankPointModifier(DamageSource ds) {
        return 0.1f;
    }

    @Override
    public float getRankPointModifier(ComboState combo) {
        return 0.1f;
    }

}
