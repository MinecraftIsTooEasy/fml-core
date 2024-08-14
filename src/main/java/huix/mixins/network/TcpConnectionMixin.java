package huix.mixins.network;


import cpw.mods.fml.common.network.FMLNetworkHandler;
import huix.injected_interfaces.IINetHandler;
import net.minecraft.network.TcpConnection;
import net.minecraft.network.packet.NetHandler;
import net.xiaoyu233.fml.util.ReflectHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin( TcpConnection.class )
public class TcpConnectionMixin {

    @Shadow
    private NetHandler theNetHandler;


    @Inject(method = "processReadPackets", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/packet/NetHandler;handleErrorMessage(Ljava/lang/String;[Ljava/lang/Object;)V", shift = At.Shift.AFTER))
    private void injectPacketClose(CallbackInfo ci) {
        FMLNetworkHandler.onConnectionClosed(ReflectHelper.dyCast(this), ((IINetHandler) this.theNetHandler).getPlayer());
    }
}
