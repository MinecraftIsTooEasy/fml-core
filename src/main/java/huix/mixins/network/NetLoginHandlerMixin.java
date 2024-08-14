package huix.mixins.network;

import cpw.mods.fml.common.network.FMLNetworkHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.TcpConnection;
import net.minecraft.network.packet.NetHandler;
import net.minecraft.network.packet.Packet1Login;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.xiaoyu233.fml.util.ReflectHelper;
import org.spongepowered.asm.mixin.*;

@Mixin( net.minecraft.network.NetLoginHandler.class )
public class NetLoginHandlerMixin extends NetHandler {

    @Shadow
    public boolean isServerHandler() {
        return false;
    }

    @Overwrite
    public void handleLogin(Packet1Login par1Packet1Login) {
        FMLNetworkHandler.handleLoginPacketOnServer(ReflectHelper.dyCast(this), par1Packet1Login);
    }

    @Shadow
    public INetworkManager getNetManager() {
        return null;
    }

    @Overwrite
    public void tryLogin() {
        if (this.field_72544_i) {
            this.initializePlayerConnection();
        }

        if (this.connectionTimer++ == 600) {
            this.raiseErrorAndDisconnect("Took too long to log in");
        } else {
            this.myTCPConnection.processReadPackets();
        }

    }

    @Shadow
    private int connectionTimer;
    @Shadow
    private volatile boolean field_72544_i;
    @Shadow
    @Final
    private MinecraftServer mcServer;
    @Shadow
    @Final
    public TcpConnection myTCPConnection;
    @Shadow
    public boolean connectionComplete;
    @Shadow
    private String clientUsername;
    @Shadow
    public void raiseErrorAndDisconnect(String par1Str) {
    }

    @Overwrite
    public void initializePlayerConnection() {
        FMLNetworkHandler.onConnectionReceivedFromClient(ReflectHelper.dyCast(this), this.mcServer, this.myTCPConnection.getSocketAddress(), this.clientUsername);
    }

    @Unique
    public void completeConnection(String s) {
        if (s != null) {
            this.raiseErrorAndDisconnect(s);
        } else {
            EntityPlayerMP var2 = this.mcServer.getConfigurationManager().createPlayerForUser(this.clientUsername);
            if (var2 != null) {
                this.mcServer.getConfigurationManager().initializeConnectionToPlayer(this.myTCPConnection, var2);
            }
        }

        this.connectionComplete = true;
    }

    @Unique
    public void handleCustomPayload(Packet250CustomPayload par1Packet250CustomPayload) {
        FMLNetworkHandler.handlePacket250Packet(par1Packet250CustomPayload, this.getNetManager(), this);
    }

}
