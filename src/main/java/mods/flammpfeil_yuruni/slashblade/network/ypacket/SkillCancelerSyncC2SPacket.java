package mods.flammpfeil_yuruni.slashblade.network.ypacket;

import mods.flammpfeil_yuruni.slashblade.SlashBlade;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class SkillCancelerSyncC2SPacket {

    private final String skillListString;

    public SkillCancelerSyncC2SPacket(List<String> skillList) {
        StringBuilder sb = new StringBuilder();
        for (String skillName : skillList) {
            sb.append(skillName + ",");
        }
        skillListString = sb.toString();
    }

    public SkillCancelerSyncC2SPacket(FriendlyByteBuf buf) {
        skillListString = buf.readUtf();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(this.skillListString);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        supplier.get().setPacketHandled(true);

        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            assert player != null;
            //Unpack string to list
            List<String> unpacked = new ArrayList<>(Arrays.asList(this.skillListString.split(",")));

            SlashBlade.serverCanceledSkillData.addNewPlayerIfAbsent(player, unpacked);
            SlashBlade.serverCanceledSkillData.replacePlayerDataIfExists(player, unpacked);
        });
        return true;
    }

}
