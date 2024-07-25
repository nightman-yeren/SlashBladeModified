package mods.flammpfeil_yuruni.slashblade.network.ypacket;

import mods.flammpfeil_yuruni.slashblade.SlashBlade;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class SkillCancelerSyncS2CPacket {

    private String skillListString;

    public SkillCancelerSyncS2CPacket(List<String> skillList) {
        StringBuilder sb = new StringBuilder();
        for (String skillName : skillList) {
            sb.append(skillName).append(",");
        }
        skillListString = sb.toString();
    }

    public SkillCancelerSyncS2CPacket(FriendlyByteBuf buf) {
        skillListString = buf.readUtf();
    }

    public void toBytes(FriendlyByteBuf buf) {
        if (this.skillListString == null) this.skillListString = "";
        buf.writeUtf(this.skillListString);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        supplier.get().setPacketHandled(true);

        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            /*
            ServerPlayer player = context.getSender();
            assert player != null;
            //Unpack string to list
            List<String> unpacked = new ArrayList<>(Arrays.asList(this.skillListString.split(",")));
            System.out.println(unpacked);

            SlashBlade.serverCanceledSkillData.addNewPlayerIfAbsent(player, unpacked);
            SlashBlade.serverCanceledSkillData.replacePlayerDataIfExists(player, unpacked);
             */
            List<String> unpacked = new ArrayList<>(Arrays.asList(this.skillListString.split(",")));

            SlashBlade.mobilitySkillCanceler.replaceCanceledList(unpacked);
        });
        return true;
    }

}
