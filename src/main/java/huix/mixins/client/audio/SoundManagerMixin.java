package huix.mixins.client.audio;


import huix.injected_interfaces.IISoundManager;
import net.minecraft.client.audio.SoundManager;
import net.minecraft.client.resources.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin( SoundManager.class )
public class SoundManagerMixin implements IISoundManager {

    @Unique
    public boolean LOAD_SOUND_SYSTEM = true;

    @Inject(method = "onResourceManagerReload", at = @At("HEAD"), cancellable = true)
    private void injectOnResourceManagerReload(ResourceManager par1ResourceManager, CallbackInfo ci) {
        if (!this.LOAD_SOUND_SYSTEM) ci.cancel(); //Stop the sound system from initializing three times at startup. Causing race conditions on some machines.
    }

    @Override
    public boolean getLOAD_SOUND_SYSTEM() {
        return this.LOAD_SOUND_SYSTEM;
    }

    @Override
    public void setLOAD_SOUND_SYSTEM(boolean value) {
        this.LOAD_SOUND_SYSTEM = value;
    }
}
