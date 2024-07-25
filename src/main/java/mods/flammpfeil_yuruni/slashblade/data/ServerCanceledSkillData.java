package mods.flammpfeil_yuruni.slashblade.data;

import net.minecraft.server.level.ServerPlayer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerCanceledSkillData {

    //New hashmap for all players
    Map<String, List<String>> playerCanceledSkillsMap;

    private static final class SingletonHolder {
        private static final ServerCanceledSkillData instance = new ServerCanceledSkillData();
    }

    public static ServerCanceledSkillData getInstance() {
        return ServerCanceledSkillData.SingletonHolder.instance;
    }

    private ServerCanceledSkillData() {
        playerCanceledSkillsMap = new HashMap<>();
    }

    public void addNewPlayer(ServerPlayer player, List<String> list) {
        playerCanceledSkillsMap.put(player.getName().getString(), list);
    }

    public void addNewPlayerIfAbsent(ServerPlayer player, List<String> list) {
        playerCanceledSkillsMap.putIfAbsent(player.getName().getString(), list);
    }

    public void replacePlayerDataIfExists(ServerPlayer player, List<String> list) {
        if (playerCanceledSkillsMap.containsKey(player.getName().getString())) {
            playerCanceledSkillsMap.replace(player.getName().getString(), list);
        }
    }

    public void removePlayer(ServerPlayer player) {
        playerCanceledSkillsMap.remove(player.getName().getString());
    }

    public boolean isSkillCanceledInPlayer(ServerPlayer player, String skill) {
        if (!playerCanceledSkillsMap.containsKey(player.getName().getString())) {
            return false;
        }
        for (String skillNames : playerCanceledSkillsMap.get(player.getName().getString())) {
            if (skill.equals(skillNames)) {
                return true;
            }
        }
        return false;
    }

    public boolean isSkillCanceledInPlayer(String playerName, String skill) {
        if (!playerCanceledSkillsMap.containsKey(playerName)) {
            return false;
        }
        for (String skillNames : playerCanceledSkillsMap.get(playerName)) {
            if (skill.equals(skillNames)) {
                return true;
            }
        }
        return false;
    }

    public List<String> getPlayerCanceledSkillsIfExists(ServerPlayer player) {
        if (playerCanceledSkillsMap.containsKey(player.getName().getString())) {
            return playerCanceledSkillsMap.get(player.getName().getString());
        }
        return null;
    }

}
