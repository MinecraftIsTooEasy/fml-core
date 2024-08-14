package huix.mixins.server.integrated;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.server.integrated.IntegratedServer;
import net.xiaoyu233.fml.util.ReflectHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin( IntegratedServer.class )
public class IntegratedServerMixin {

    @Inject(method = "startServer", at = @At(value = "TAIL"), cancellable = true)
    private void injection_0(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(FMLCommonHandler.instance().handleServerStarting(ReflectHelper.dyCast(this)));
    }

    @Inject(method = "startServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/integrated/IntegratedServer;setKeyPair(Ljava/security/KeyPair;)V"
                                    , shift = At.Shift.AFTER), cancellable = true)
    private void injection_1(CallbackInfoReturnable<Boolean> cir) {
        if (!FMLCommonHandler.instance().handleServerAboutToStart(ReflectHelper.dyCast(this))) {
            cir.setReturnValue(false);
        }
    }
}
