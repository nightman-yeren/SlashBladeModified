package mods.flammpfeil_yuruni.slashblade.event.client;

import mods.flammpfeil_yuruni.slashblade.SlashBlade;
import mods.flammpfeil_yuruni.slashblade.memory.ServerMemory;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class PlayerConnection {

    private static final class PlayerConnectionInstance {
        private static final PlayerConnection instance = new PlayerConnection();
    }

    public static PlayerConnection getInstance() {
        return PlayerConnectionInstance.instance;
    }

    public void register() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onPlayerJoinEvent(PlayerEvent.PlayerLoggedInEvent event) {
        SlashBlade.serverMemory.setCurrentServer(event.getEntity().getServer());
    }

    @SubscribeEvent
    public void onPlayerLeaveEvent(PlayerEvent.PlayerLoggedOutEvent event) {
        SlashBlade.serverMemory.setCurrentServer(null);
    }

}
