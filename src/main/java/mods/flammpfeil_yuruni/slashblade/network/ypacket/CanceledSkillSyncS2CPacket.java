package mods.flammpfeil_yuruni.slashblade.network.ypacket;

import mods.flammpfeil_yuruni.slashblade.client.data.ClientCanceledSkillData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class CanceledSkillSyncS2CPacket {

    private final String canceledSkills;

    public CanceledSkillSyncS2CPacket(String canceledSkills) {
        this.canceledSkills = canceledSkills;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(canceledSkills);
    }

    public CanceledSkillSyncS2CPacket(FriendlyByteBuf buf) {
        this.canceledSkills = buf.readUtf();
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            List<String> canceledSkillsList = List.of(canceledSkills.split(","));
            List<String> newList = new ArrayList<>();
            for (String item : canceledSkillsList) {
                String replace = item.replace("[", "").replace("]", "").replace(" ", "");
                newList.add(replace);
            }
            ClientCanceledSkillData.set(newList);
        });
        return true;
    }

}
