package mods.flammpfeil_yuruni.slashblade.event.client;

import mods.flammpfeil_yuruni.slashblade.SlashBlade;
import mods.flammpfeil_yuruni.slashblade.capability.bladecharge.BladeChargeProvider;
import mods.flammpfeil_yuruni.slashblade.network.YMessages;
import mods.flammpfeil_yuruni.slashblade.network.ypacket.BladeChargeSyncS2CPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
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
        Player currentPlayer = Minecraft.getInstance().player;
        if (currentPlayer == null) return;
        if (event.getEntity().getName().toString().equals(currentPlayer.getName().toString())) {
            SlashBlade.serverMemory.setCurrentServer(null);
        }
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (!(event.getServer() == null)) {
            SlashBlade.serverMemory.setCurrentServer(event.getServer());
        }
    }

    @SubscribeEvent
    public void onPlayerJoinWorld(EntityJoinLevelEvent event) {
        if (!event.getLevel().isClientSide) {
            if (event.getEntity() instanceof ServerPlayer player) {
                player.getCapability(BladeChargeProvider.BLADE_CHARGE).ifPresent(bladeCharge -> {
                    YMessages.sendToPlayer(new BladeChargeSyncS2CPacket(bladeCharge.getPowerCharges()), player);
                });
            }
        }
    }

}
