package mods.flammpfeil_yuruni.slashblade.client;

import mods.flammpfeil_yuruni.slashblade.network.YMessages;
import mods.flammpfeil_yuruni.slashblade.network.ypacket.SkillCancelerSyncC2SPacket;
import mods.flammpfeil_yuruni.slashblade.util.InputCommand;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class MobilitySkillCanceler {

    public enum MobilitySkills {

        RAPID_SLASH("ex_rapid_slash", EnumSet.of(InputCommand.SNEAK, InputCommand.FORWARD, InputCommand.R_DOWN), true),
        SLAYER_STYLE_ARTS("slayer_style_arts", EnumSet.of(InputCommand.SPRINT), true),
        JUDGEMENT_CUT("ex_judgement_cut", EnumSet.of(InputCommand.SAVE_TOOLBAR), false);

        public final String name;
        public final EnumSet<InputCommand> commands;
        public final boolean include;

        MobilitySkills(String name, EnumSet<InputCommand> commands, boolean include) {
            this.name = name;
            this.commands = commands;
            this.include = include;
        }
    }

    List<String> canceledMobilitySkills;

    public MobilitySkillCanceler() {
        this.canceledMobilitySkills = new ArrayList<>();
    }

    public void addCanceledMobilitySkill(String name) {
        if (!canceledMobilitySkills.contains(name)) canceledMobilitySkills.add(name);
        YMessages.sendToServer(new SkillCancelerSyncC2SPacket(canceledMobilitySkills));
    }

    public void cancelMobilitySkillTemporarily(String name, int milliseconds) {
        addCanceledMobilitySkill(name);
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        removeCanceledMobilitySkill(name);
                    }
                },
                milliseconds
        );
    }

    public void removeCanceledMobilitySkill(String name) {
        if (canceledMobilitySkills.contains(name)) canceledMobilitySkills.remove(name);
        YMessages.sendToServer(new SkillCancelerSyncC2SPacket(canceledMobilitySkills));
    }

    public void clearCanceledMobilitySkill() {
        canceledMobilitySkills = new ArrayList<>();
        YMessages.sendToServer(new SkillCancelerSyncC2SPacket(canceledMobilitySkills));
    }

    public boolean isMobilitySkillCanceled(String name) {
        return canceledMobilitySkills.contains(name);
    }

    public List<String> getCanceledMobilitySkills() {
        return canceledMobilitySkills;
    }

    public void replaceCanceledList(List<String> list) {
        this.canceledMobilitySkills = list;
        YMessages.sendToServer(new SkillCancelerSyncC2SPacket(canceledMobilitySkills));
    }

}
