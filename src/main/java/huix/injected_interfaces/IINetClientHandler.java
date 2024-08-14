package huix.injected_interfaces;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.packet.Packet131MapData;
import net.minecraft.network.packet.Packet250CustomPayload;

public interface IINetClientHandler {
    default void handleVanilla250Packet(Packet250CustomPayload par1Packet250CustomPayload) {
    }

    default void fmlPacket131Callback(Packet131MapData par1Packet131MapData) {
    }

    default EntityPlayer getPlayer() {
        return null;
    }
}
