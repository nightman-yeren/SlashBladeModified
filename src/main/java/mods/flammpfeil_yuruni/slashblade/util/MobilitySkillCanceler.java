package mods.flammpfeil_yuruni.slashblade.util;

import mods.flammpfeil_yuruni.slashblade.network.YMessages;
import mods.flammpfeil_yuruni.slashblade.network.ypacket.CanceledSkillSyncS2CPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;

public class MobilitySkillCanceler {

    public enum MobilitySkills {

        RAPID_SLASH("ex_rapid_slash"),
        SLAYER_STYLE_ARTS("slayer_style_arts"),
        JUDGEMENT_CUT("ex_judgement_cut");

        public final String name;

        MobilitySkills(String name) {
            this.name = name;
        }
    }

    public MobilitySkillCanceler() {

    }

    public List<String> getAllCanceledMobilitySkills(Player player) {
        //return new ArrayList<>(player.getPersistentData().getAllKeys());
        List<String> canceledSkills = new ArrayList<>();
        for (String key : player.getPersistentData().getAllKeys()) {
            if (key.contains("-slashbladeCanceledSkill-") && key.contains(player.getName().getString())) {
                canceledSkills.add(key.split("-")[key.split("-").length - 1].replace("[", "").replace("]", "").replace(" ", ""));
            }
        }
        return canceledSkills;
    }

    public String convertStringListToString(List<String> list) {
        StringBuilder result = new StringBuilder();
        for (String item : list) {
            result.append(",").append(item);
        }
        return result.toString();
    }

    public void addCanceledMobilitySkill(String name, ServerPlayer player) {
        //if (!canceledMobilitySkills.contains(name)) canceledMobilitySkills.add(name);
        player.getPersistentData().putString(player.getName().getString() + "-slashbladeCanceledSkill-" + name, "1");
        YMessages.sendToPlayer(new CanceledSkillSyncS2CPacket(String.valueOf(getAllCanceledMobilitySkills(player))), player);
    }

    public void cancelMobilitySkillTemporarily(String name, ServerPlayer player, int milliseconds) {
        addCanceledMobilitySkill(name, player);
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        removeCanceledMobilitySkill(name, player);
                    }
                },
                milliseconds
        );
    }

    public void removeCanceledMobilitySkill(String name, ServerPlayer player) {
        //if (canceledMobilitySkills.contains(name)) canceledMobilitySkills.remove(name);
        player.getPersistentData().remove(player.getName().getString() + "-slashbladeCanceledSkill-" + name);
        YMessages.sendToPlayer(new CanceledSkillSyncS2CPacket(String.valueOf(getAllCanceledMobilitySkills(player))), player);
    }

    //public void clearCanceledMobilitySkill() {
        //canceledMobilitySkills = new ArrayList<>();
    //}

    public boolean isMobilitySkillCanceled(String name, ServerPlayer player) {
        //player.sendSystemMessage(Component.literal(player.getName().getString() + " requested skill method call, and the result is " + player.getPersistentData().contains(player.getName().getString() + "-slashbladeCanceledSkill-" + name)).withStyle(ChatFormatting.AQUA));
        return player.getPersistentData().contains(player.getName().getString() + "-slashbladeCanceledSkill-" + name);
    }

}
