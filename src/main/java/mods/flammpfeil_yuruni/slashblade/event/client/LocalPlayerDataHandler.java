package mods.flammpfeil_yuruni.slashblade.event.client;

import mods.flammpfeil_yuruni.slashblade.SlashBlade;
import mods.flammpfeil_yuruni.slashblade.util.FileUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class LocalPlayerDataHandler {

    private static final class PlayerDataInstance {
        private static final LocalPlayerDataHandler instance = new LocalPlayerDataHandler();
    }

    public static LocalPlayerDataHandler getInstance() {
        return PlayerDataInstance.instance;
    }

    public void register() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onPlayerJoinEvent(PlayerEvent.PlayerLoggedInEvent event) {
        String hitPlayerString = FileUtils.loadDataFromFile("hitRulePlayer", "true");
        boolean hitPlayer = false;
        if (!hitPlayerString.isEmpty()) {
            hitPlayer = Boolean.parseBoolean(hitPlayerString);
        }
        SlashBlade.hitRuleMemory.setHitRulePlayer(hitPlayer);
        String hitPassiveString = FileUtils.loadDataFromFile("hitRulePassive", "true");
        boolean hitPassive = false;
        if (!hitPassiveString.isEmpty()) {
            hitPassive = Boolean.parseBoolean(hitPassiveString);
        }
        SlashBlade.hitRuleMemory.setHitRulePassive(hitPassive);
        String hitAggressiveString = FileUtils.loadDataFromFile("hitRuleAggressive", "true");
        boolean hitAggressive = false;
        if (!hitAggressiveString.isEmpty()) {
            hitAggressive = Boolean.parseBoolean(hitAggressiveString);
        }
        SlashBlade.hitRuleMemory.setHitRuleAggressive(hitAggressive);
        String hitRuleString = FileUtils.loadDataFromFile("hitRule", "false");
        boolean hitRule = false;
        if (!hitRuleString.isEmpty()) {
            hitRule = Boolean.parseBoolean(hitRuleString);
        }
        SlashBlade.hitRuleMemory.setHitRuleAggressive(hitRule);
        //Set current player
        SlashBlade.hitRuleMemory.setCurrentPlayer(event.getEntity());
    }

}
