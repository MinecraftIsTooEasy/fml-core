package huix.mixins.client.settings;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.client.resources.ResourceManager;
import net.minecraft.client.settings.GameSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin( GameSettings.class )
public class GameSettingsMixin {

    @Inject(method = "saveOptions", at = @At(value = "HEAD"), cancellable = true)
    private void injectSaveOptions(CallbackInfo ci) {
        if (FMLClientHandler.instance().isLoading()) ci.cancel();
    }

}
