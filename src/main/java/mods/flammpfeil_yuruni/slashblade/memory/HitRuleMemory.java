package mods.flammpfeil_yuruni.slashblade.memory;

public class HitRuleMemory {

    boolean hitRulePlayer = false;
    boolean hitRulePassive = false;
    boolean hitRuleAggressive = false;

    private static final class hitRuleMemoryHolder {
        private static final HitRuleMemory instance = new HitRuleMemory();
    }

    public static HitRuleMemory getInstance() {
        return hitRuleMemoryHolder.instance;
    }

    public boolean isHitRulePlayer() {
        return hitRulePlayer;
    }

    public boolean isHitRulePassive() {
        return hitRulePassive;
    }

    public boolean isHitRuleAggressive() {
        return hitRuleAggressive;
    }

    public void setHitRulePlayer(boolean hitRulePlayer) {
        this.hitRulePlayer = hitRulePlayer;
    }

    public void setHitRulePassive(boolean hitRulePassive) {
        this.hitRulePassive = hitRulePassive;
    }

    public void setHitRuleAggressive(boolean hitRuleAggressive) {
        this.hitRuleAggressive = hitRuleAggressive;
    }
}
