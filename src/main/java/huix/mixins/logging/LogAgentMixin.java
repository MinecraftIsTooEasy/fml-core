//package huix.mixins.logging;
//
//
//import cpw.mods.fml.common.FMLLog;
//import cpw.mods.fml.common.registry.GameData;
//import net.minecraft.logging.LogAgent;
//import net.minecraft.logging.LogFormatter;
//import net.xiaoyu233.fml.util.ReflectHelper;
//import org.spongepowered.asm.mixin.Final;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.Overwrite;
//import org.spongepowered.asm.mixin.Shadow;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.Redirect;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//
//import java.util.logging.*;
//
//@Mixin( LogAgent.class )
//public class LogAgentMixin {
//
//    @Shadow
//    @Final
//    private Logger serverLogger;
////    @Shadow
////    @Final
////    private String logFile;
////    @Shadow
////    @Final
////    private String loggerName;
////    @Shadow
////    @Final
////    private String loggerPrefix;
//
//    @Inject(method = "setupLogger", at = @At(value = "INVOKE", target = "Ljava/util/logging/Logger;setUseParentHandlers(Z)V"))
//    private void Injection(CallbackInfo ci)   {
//        this.serverLogger.setParent(FMLLog.getLogger());
//    }
//
////    @Overwrite
////    private void setupLogger() {
////        this.serverLogger.setParent(FMLLog.getLogger());
////        Handler[] var1 = this.serverLogger.getHandlers();
////
////        for (Handler var4 : var1) {
////            this.serverLogger.removeHandler(var4);
////        }
////
////        try {
////            FileHandler var8 = new FileHandler(this.logFile, true);
////            //need fix
////            var8.setFormatter(new LogFormatter(this, null));
////            this.serverLogger.addHandler(var8);
////        } catch (Exception e) {
////            this.serverLogger.log(Level.WARNING, "Failed to log " + this.loggerName + " to " + this.logFile, e);
////        }
////
////    }
//}
