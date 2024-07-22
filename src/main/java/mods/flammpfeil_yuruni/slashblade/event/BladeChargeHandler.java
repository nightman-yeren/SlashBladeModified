package mods.flammpfeil_yuruni.slashblade.event;

import mods.flammpfeil_yuruni.slashblade.SlashBlade;
import mods.flammpfeil_yuruni.slashblade.capability.powerrank.BladeChargeProvider;
import mods.flammpfeil_yuruni.slashblade.util.MobilitySkillCanceler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;

public class BladeChargeHandler {

    private static final class SingletonHolder {
        private static final BladeChargeHandler instance = new BladeChargeHandler();
    }

    public static BladeChargeHandler getInstance() {
        return SingletonHolder.instance;
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.side == LogicalSide.SERVER) {
            event.player.getCapability(BladeChargeProvider.BLADE_CHARGE).ifPresent(charge -> {
                //System.out.println(SlashBlade.mobilitySkillCanceler.getCanceledMobilitySkills());
                if (charge.getPowerCharges() == 0) {
                    SlashBlade.mobilitySkillCanceler.addCanceledMobilitySkill(MobilitySkillCanceler.MobilitySkills.RAPID_SLASH.name);
                    SlashBlade.mobilitySkillCanceler.addCanceledMobilitySkill(MobilitySkillCanceler.MobilitySkills.SLAYER_STYLE_ARTS.name);
                    SlashBlade.mobilitySkillCanceler.addCanceledMobilitySkill(MobilitySkillCanceler.MobilitySkills.JUDGEMENT_CUT.name);
                } else if (charge.getPowerCharges() >= 1) {
                    SlashBlade.mobilitySkillCanceler.removeCanceledMobilitySkill(MobilitySkillCanceler.MobilitySkills.RAPID_SLASH.name);
                    SlashBlade.mobilitySkillCanceler.removeCanceledMobilitySkill(MobilitySkillCanceler.MobilitySkills.SLAYER_STYLE_ARTS.name);
                    SlashBlade.mobilitySkillCanceler.removeCanceledMobilitySkill(MobilitySkillCanceler.MobilitySkills.JUDGEMENT_CUT.name);
                }
            });
        }
    }

    public void register() {
        MinecraftForge.EVENT_BUS.register(this);
    }

}