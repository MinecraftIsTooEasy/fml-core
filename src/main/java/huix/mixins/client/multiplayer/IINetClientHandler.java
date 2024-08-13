package huix.mixins.client.multiplayer;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.packet.Packet250CustomPayload;

public interface IINetClientHandler {
    default void handleVanilla250Packet(Packet250CustomPayload par1Packet250CustomPayload) {
    }

    default EntityPlayer getPlayer() {
        return null;
    }
}
