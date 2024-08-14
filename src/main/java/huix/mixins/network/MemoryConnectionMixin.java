package huix.mixins.network;

import com.google.common.collect.Queues;
import cpw.mods.fml.common.network.FMLNetworkHandler;
import huix.injected_interfaces.IINetHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.main.Main;
import net.minecraft.client.multiplayer.NetClientHandler;
import net.minecraft.logging.ILogAgent;
import net.minecraft.network.MemoryConnection;
import net.minecraft.network.packet.NetHandler;
import net.minecraft.network.packet.Packet;
import net.xiaoyu233.fml.util.ReflectHelper;
import org.spongepowered.asm.mixin.*;

import java.util.Queue;

@Mixin( MemoryConnection.class )
public class MemoryConnectionMixin {
    @Unique
    private final Queue<Packet> readPacketCache_forge = Queues.newConcurrentLinkedQueue();

    @Shadow
    private NetHandler myNetHandler;
    @Shadow
    private String shutdownReason;
    @Shadow
    private Object[] field_74439_g;
    @Shadow
    @Final
    private ILogAgent field_98214_c;
    @Shadow
    private boolean shuttingDown;

    @Overwrite
    public void processReadPackets() {
        boolean is_MITE_DS_client_player = Main.is_MITE_DS && this.myNetHandler instanceof NetClientHandler;
        int var1 = 2500;

        while(var1-- >= 0 && !this.readPacketCache_forge.isEmpty()) {
            Packet var2 = readPacketCache_forge.poll();
            if (!is_MITE_DS_client_player || Main.isPacketThatMITEDSClientPlayerCanSendOrReceive(var2)) {
                long before = System.currentTimeMillis();
                var2.processPacket(this.myNetHandler);
                long delay = System.currentTimeMillis() - before;
                if (delay > 4L) {
                    Minecraft.MITE_log.logInfo((this.myNetHandler instanceof NetClientHandler ? "[Client]" : "[Server]") + " Long time processing packet (delay=" + delay + "ms, packet id=" + var2.getPacketId() + ")");
                }
            }
        }

        if (this.readPacketCache_forge.size() > var1) {
            this.field_98214_c.logWarning("Memory connection overburdened; after processing 2500 packets, we still have " + this.readPacketCache_forge.size() + " to go!");
        }

        if (this.shuttingDown && this.readPacketCache_forge.isEmpty()) {
            this.myNetHandler.handleErrorMessage(this.shutdownReason, this.field_74439_g);
        }

        FMLNetworkHandler.onConnectionClosed(ReflectHelper.dyCast(this), ((IINetHandler) this.myNetHandler).getPlayer());
    }

    @Overwrite
    public void processOrCachePacket(Packet par1Packet) {
        if (!Main.isPacketIgnored(this.myNetHandler, par1Packet)) {
            if (par1Packet.canProcessAsync() && this.myNetHandler.canProcessPacketsAsync()) {
                par1Packet.processPacket(this.myNetHandler);
            } else {
                this.readPacketCache_forge.add(par1Packet);
            }

        }
    }

    @Overwrite
    public int clearReceivedPackets() {
        int num_packets = this.readPacketCache_forge.size();
        this.readPacketCache_forge.clear();
        return num_packets;
    }
}
