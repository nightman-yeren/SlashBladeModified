package mods.flammpfeil_yuruni.slashblade.event;

import mods.flammpfeil_yuruni.slashblade.SlashBlade;
import mods.flammpfeil_yuruni.slashblade.capability.bladecharge.BladeChargeProvider;
import mods.flammpfeil_yuruni.slashblade.client.MobilitySkillCanceler;
import mods.flammpfeil_yuruni.slashblade.network.YMessages;
import mods.flammpfeil_yuruni.slashblade.network.ypacket.SkillCancelerSyncS2CPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

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
                    //SlashBlade.mobilitySkillCanceler.addCanceledMobilitySkill(MobilitySkillCanceler.MobilitySkills.RAPID_SLASH.name);
                    //SlashBlade.mobilitySkillCanceler.addCanceledMobilitySkill(MobilitySkillCanceler.MobilitySkills.SLAYER_STYLE_ARTS.name);
                    //SlashBlade.mobilitySkillCanceler.addCanceledMobilitySkill(MobilitySkillCanceler.MobilitySkills.JUDGEMENT_CUT.name);
                    List<String> addedSkillCancels = new ArrayList<>();
                    addedSkillCancels.add(MobilitySkillCanceler.MobilitySkills.RAPID_SLASH.name);
                    addedSkillCancels.add(MobilitySkillCanceler.MobilitySkills.SLAYER_STYLE_ARTS.name);
                    if (SlashBlade.serverCanceledSkillData.getPlayerCanceledSkillsIfExists((ServerPlayer) event.player) == null) {
                        SlashBlade.serverCanceledSkillData.addNewPlayerIfAbsent((ServerPlayer) event.player, new ArrayList<>());
                    }
                    List<String> combinedList = new ArrayList<>(Stream.concat(addedSkillCancels.stream(), SlashBlade.serverCanceledSkillData.getPlayerCanceledSkillsIfExists((ServerPlayer) event.player).stream()).toList());
                    //Remove duplicates
                    Set<String> combinedSet = new HashSet<>(combinedList);
                    combinedList.clear();
                    combinedList.addAll(combinedSet);
                    YMessages.sendToPlayer(new SkillCancelerSyncS2CPacket(combinedList), (ServerPlayer) event.player);
                } else if (charge.getPowerCharges() >= 1) {
                    //SlashBlade.mobilitySkillCanceler.removeCanceledMobilitySkill(MobilitySkillCanceler.MobilitySkills.RAPID_SLASH.name);
                    //SlashBlade.mobilitySkillCanceler.removeCanceledMobilitySkill(MobilitySkillCanceler.MobilitySkills.SLAYER_STYLE_ARTS.name);
                    //SlashBlade.mobilitySkillCanceler.removeCanceledMobilitySkill(MobilitySkillCanceler.MobilitySkills.JUDGEMENT_CUT.name);
                    List<String> dataCopy = SlashBlade.serverCanceledSkillData.getPlayerCanceledSkillsIfExists((ServerPlayer) event.player);
                    if (dataCopy == null) {
                        SlashBlade.serverCanceledSkillData.addNewPlayerIfAbsent((ServerPlayer) event.player, new ArrayList<>());
                        dataCopy = new ArrayList<>();
                        dataCopy.add(MobilitySkillCanceler.MobilitySkills.RAPID_SLASH.name);
                        dataCopy.add(MobilitySkillCanceler.MobilitySkills.SLAYER_STYLE_ARTS.name);
                    }
                    dataCopy.remove(MobilitySkillCanceler.MobilitySkills.RAPID_SLASH.name);
                    dataCopy.remove(MobilitySkillCanceler.MobilitySkills.SLAYER_STYLE_ARTS.name);
                    dataCopy.remove(MobilitySkillCanceler.MobilitySkills.JUDGEMENT_CUT.name);
                    YMessages.sendToPlayer(new SkillCancelerSyncS2CPacket(dataCopy), (ServerPlayer) event.player);
                }
            });
        }
    }

    public void register() {
        MinecraftForge.EVENT_BUS.register(this);
    }

}
