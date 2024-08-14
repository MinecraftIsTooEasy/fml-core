package huix.mixins.network;

import com.llamalad7.mixinextras.sugar.Local;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.network.FMLNetworkHandler;
import net.minecraft.network.NetServerHandler;
import net.minecraft.network.NetworkListenThread;
import net.xiaoyu233.fml.util.ReflectHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.logging.Level;

@Mixin( NetworkListenThread.class )
public class NetworkListenThreadMixin {

    @Inject(method = "networkTick", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/logging/ILogAgent;logWarningException(Ljava/lang/String;Ljava/lang/Throwable;)V", shift = At.Shift.AFTER))
    private void injectPacketError(CallbackInfo ci, @Local Exception exception, @Local NetServerHandler netServerHandler) {
        FMLLog.log(Level.SEVERE, exception, "A critical server error occured handling a packet, kicking %s", netServerHandler.playerEntity.getEntityName());
    }
}
