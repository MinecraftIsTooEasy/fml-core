package huix.mixins.server.dedicated;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.server.dedicated.DedicatedServer;
import net.xiaoyu233.fml.util.ReflectHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin( DedicatedServer.class )
public class DedicatedServerMixin {

    @Inject(method = "startServer", at =
    @At(value = "INVOKE", target = "Lnet/minecraft/logging/ILogAgent;logInfo(Ljava/lang/String;)V", ordinal = 2, shift = At.Shift.BEFORE))
    private void injection_0(CallbackInfoReturnable<Boolean> cir) {
        FMLCommonHandler.instance().onServerStart(ReflectHelper.dyCast(this));
    }

    @Inject(method = "startServer", at =
        @At(value = "INVOKE", target = "Lnet/minecraft/server/dedicated/DedicatedServer;setConfigurationManager(Lnet/minecraft/server/management/ServerConfigurationManager;)V", shift = At.Shift.BEFORE))
    private void injection_1(CallbackInfoReturnable<Boolean> cir) {
        FMLCommonHandler.instance().onServerStarted();
    }

    @Inject(method = "startServer", at =
    @At(value = "INVOKE", target = "Lnet/minecraft/server/dedicated/PropertyManager;setProperty(Ljava/lang/String;Ljava/lang/Object;)V", ordinal = 1, shift = At.Shift.BEFORE), cancellable = true)
    private void injection_2(CallbackInfoReturnable<Boolean> cir) {
        if (!FMLCommonHandler.instance().handleServerAboutToStart(ReflectHelper.dyCast(this))) {
             cir.setReturnValue(false);
        }
    }

    @Inject(method = "startServer", at = @At(value = "TAIL"), cancellable = true)
    private void injection_3(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(FMLCommonHandler.instance().handleServerStarting(ReflectHelper.dyCast(this)));
    }

}
