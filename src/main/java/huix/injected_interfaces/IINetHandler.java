package huix.injected_interfaces;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.packet.Packet250CustomPayload;

public interface IINetHandler {

    default void completeConnection(String s) {

    }

    default void handleVanilla250Packet(Packet250CustomPayload payload) {

    }

    default EntityPlayer getPlayer() {
        return null;
    }

}
