package mods.flammpfeil_yuruni.slashblade.event;

import mods.flammpfeil_yuruni.slashblade.SlashBlade;
import mods.flammpfeil_yuruni.slashblade.commands.ContributeAndStory;
import mods.flammpfeil_yuruni.slashblade.commands.ExcludePlayerLock;
import mods.flammpfeil_yuruni.slashblade.commands.GetExcludedPlayers;
import mods.flammpfeil_yuruni.slashblade.commands.RemoveExcludedPlayers;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.server.command.ConfigCommand;

public class CommandsHandler {

    private static final class CommandsHolder {
        private static final CommandsHandler instance = new CommandsHandler();
    }

    public static CommandsHandler getInstance() {
        return CommandsHolder.instance;
    }

    @SubscribeEvent
    public void onCommandRegister(RegisterCommandsEvent event) {
        new ExcludePlayerLock(event.getDispatcher());
        new RemoveExcludedPlayers(event.getDispatcher());
        new GetExcludedPlayers(event.getDispatcher());
        new ContributeAndStory(event.getDispatcher());

        ConfigCommand.register(event.getDispatcher());
    }

    @SubscribeEvent
    public void onPlayerCloneEvent(PlayerEvent.Clone event) {
        if (!event.getOriginal().level().isClientSide()) {
            event.getEntity().getPersistentData().putString(SlashBlade.modid + "excludedPlayers",
                    event.getOriginal().getPersistentData().getString(SlashBlade.modid + "excludedPlayers"));
        }
    }


    public void register() {
        MinecraftForge.EVENT_BUS.register(this);
    }
}