package huix.mixins.server;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.network.NetLoginHandler;
import net.minecraft.server.ServerListenThread;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import cpw.mods.fml.common.FMLLog;

import java.util.logging.Level;

@Mixin( ServerListenThread.class )
public class ServerListenThreadMixin {

    @Inject(method = "processPendingConnections", at =
        @At(value = "INVOKE", target = "Lnet/minecraft/network/NetLoginHandler;raiseErrorAndDisconnect(Ljava/lang/String;)V", shift = At.Shift.AFTER))
    private void injection(CallbackInfo ci, @Local Exception exception, @Local NetLoginHandler netLoginHandler) {
        FMLLog.log(Level.SEVERE, exception, "Error handling login related packet - connection from %s refused", netLoginHandler.getUsernameAndAddress());
    }

}
