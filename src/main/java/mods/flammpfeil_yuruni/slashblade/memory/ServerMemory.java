package mods.flammpfeil_yuruni.slashblade.memory;

import net.minecraft.server.MinecraftServer;

public class ServerMemory {
    MinecraftServer currentServer = null;

    private static final class ServerMemoryHolder {
        private static final ServerMemory instance = new ServerMemory();
    }

    public static ServerMemory getInstance() {
        return ServerMemoryHolder.instance;
    }

    public MinecraftServer getCurrentServer() {
        return currentServer;
    }

    public void setCurrentServer(MinecraftServer currentServer) {
        this.currentServer = currentServer;
    }
}
