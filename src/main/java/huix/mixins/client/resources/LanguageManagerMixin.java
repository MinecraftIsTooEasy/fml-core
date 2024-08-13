package huix.mixins.client.resources;

import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.resources.LanguageManager;
import net.minecraft.client.resources.Locale;
import net.minecraft.client.resources.ResourceManager;
import net.minecraft.src.FMLRenderAccessLibrary;
import net.xiaoyu233.fml.util.ReflectHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin( LanguageManager.class )
public class LanguageManagerMixin {

    @Shadow
    private String currentLanguage;
    @Shadow
    @Final
    protected static Locale currentLocale;

    @Inject(method = "onResourceManagerReload", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/StringTranslate;func_135063_a(Ljava/util/Map;)V"
            , shift = At.Shift.BEFORE))
    private void injectOnResourceManagerReload(ResourceManager par1ResourceManager, CallbackInfo ci) {
        LanguageRegistry.instance().loadLanguageTable(currentLocale.field_135032_a, this.currentLanguage);
    }
}
