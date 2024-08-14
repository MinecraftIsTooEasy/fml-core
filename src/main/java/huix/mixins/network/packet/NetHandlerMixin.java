package huix.mixins.network.packet;

import huix.injected_interfaces.IINetHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.packet.NetHandler;
import net.minecraft.network.packet.Packet250CustomPayload;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(NetHandler.class)
public class NetHandlerMixin implements IINetHandler {

    @Unique
    @Override
    public void handleVanilla250Packet(Packet250CustomPayload payload) {

    }

    @Unique
    @Override
    public EntityPlayer getPlayer() {
        return null;
    }
}
