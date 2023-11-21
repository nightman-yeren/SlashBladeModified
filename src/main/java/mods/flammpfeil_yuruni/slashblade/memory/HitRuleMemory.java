package mods.flammpfeil_yuruni.slashblade.memory;

import net.minecraft.world.entity.player.Player;

public class HitRuleMemory {

    boolean hitRulePlayer = false;
    boolean hitRulePassive = false;
    boolean hitRuleAggressive = false;
    Player currentPlayer = null;
    //ENABLES HIT RULE - CURRENTLY BUGGY
    boolean hitRuleEnabled = false;

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

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public boolean isHitRuleEnabled() {
        return hitRuleEnabled;
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

    public void setHitRuleEnabled(boolean hitRuleEnabled) {
        this.hitRuleEnabled = hitRuleEnabled;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }
}
