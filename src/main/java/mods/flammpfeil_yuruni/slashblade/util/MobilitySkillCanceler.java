package mods.flammpfeil_yuruni.slashblade.util;

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

    List<String> canceledMobilitySkills;

    public MobilitySkillCanceler() {
        this.canceledMobilitySkills = new ArrayList<>();
    }

    public void addCanceledMobilitySkill(String name) {
        if (!canceledMobilitySkills.contains(name)) canceledMobilitySkills.add(name);
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
    }

    public void clearCanceledMobilitySkill() {
        canceledMobilitySkills = new ArrayList<>();
    }

    public boolean isMobilitySkillCanceled(String name) {
        return canceledMobilitySkills.contains(name);
    }

    public List<String> getCanceledMobilitySkills() {
        return canceledMobilitySkills;
    }

}
