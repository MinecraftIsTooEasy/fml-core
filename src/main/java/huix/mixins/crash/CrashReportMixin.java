package huix.mixins.crash;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.xiaoyu233.fml.util.ReflectHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin( CrashReport.class)
public class CrashReportMixin {

    @Shadow
    @Final
    private CrashReportCategory field_85061_c;

    @Inject(method = "populateEnvironment", at = @At(value = "RETURN"))
    private void injectStartLoading(CallbackInfo ci) {
        FMLCommonHandler.instance().enhanceCrashReport(ReflectHelper.dyCast(this), this.field_85061_c);
    }
}
