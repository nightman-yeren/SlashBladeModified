package mods.flammpfeil_yuruni.slashblade.event.client;

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
    }

}
