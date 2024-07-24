package mods.flammpfeil_yuruni.slashblade.client.data;

import java.util.ArrayList;
import java.util.List;

public class ClientCanceledSkillData {

    private static List<String> canceledSkills = new ArrayList<>();

    public static void add(String name) {
        ClientCanceledSkillData.canceledSkills.add(name);
    }

    public static void remove(String name) {
        ClientCanceledSkillData.canceledSkills.remove(name);
    }

    public static void clear() {
        ClientCanceledSkillData.canceledSkills = new ArrayList<>();
    }

    public static void set(List<String> list) {
        ClientCanceledSkillData.canceledSkills = list;
    }

    public static boolean isSkillCanceled(String skill) {
        return ClientCanceledSkillData.canceledSkills.contains(skill);
    }

}
