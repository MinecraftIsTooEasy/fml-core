package huix.mixins.client;

import com.google.common.collect.MapDifference;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.ItemData;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import huix.mixins.client.audio.IISoundManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundManager;
import net.minecraft.client.gui.LoadingScreenRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.ReloadableResourceManager;
import net.minecraft.client.resources.ResourceManager;
import net.minecraft.util.Timer;
import net.xiaoyu233.fml.util.ReflectHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin( Minecraft.class )
public class MinecraftMixin {

    @Shadow
    public SoundManager sndManager;
    @Shadow
    private List defaultResourcePacks;
    @Shadow
    private ReloadableResourceManager mcResourceManager;
    @Shadow
    private Timer timer;
    @Shadow
    private boolean integratedServerIsRunning;
    @Shadow
    public LoadingScreenRenderer loadingScreen;


    @Inject(method = "startGame", at = @At(value = "NEW", target = "(Lnet/minecraft/client/resources/ResourceManager;Lnet/minecraft/client/settings/GameSettings;Ljava/io/File;)Lnet/minecraft/client/audio/SoundManager;"
                                            , shift = At.Shift.AFTER))
    private void injectSoundSystem(CallbackInfo ci) {
        ((IISoundManager) this.sndManager).setLOAD_SOUND_SYSTEM(false);
    }

    @Inject(method = "startGame", at = @At(value = "NEW", target = "(Lnet/minecraft/client/settings/GameSettings;Lnet/minecraft/util/ResourceLocation;Lnet/minecraft/client/renderer/texture/TextureManager;Z)Lnet/minecraft/client/gui/FontRenderer;"
            , ordinal = 0, shift = At.Shift.AFTER))
    private void injectStartLoading(CallbackInfo ci) {
        FMLClientHandler.instance().beginMinecraftLoading(ReflectHelper.dyCast(this), this.defaultResourcePacks, this.mcResourceManager);
    }

    @Inject(method = "startGame", at = @At(value = "NEW", target = "(Lnet/minecraft/world/World;Lnet/minecraft/client/renderer/texture/TextureManager;)Lnet/minecraft/client/particle/EffectRenderer;"
            , shift = At.Shift.AFTER))
    private void injectFinishedLoading(CallbackInfo ci) {
        FMLClientHandler.instance().finishMinecraftLoading();
    }

    @Inject(method = "startGame", at = @At(value = "RETURN"))
    private void injectInitializationComplete(CallbackInfo ci) {
        FMLClientHandler.instance().onInitializationComplete();
    }

    @Inject(method = "runGameLoop", at = @At(value = "INVOKE", target = "Lnet/minecraft/profiler/Profiler;endStartSection(Ljava/lang/String;)V"
            , ordinal = 2, shift = At.Shift.AFTER))
    private void injectRenderTickStart(CallbackInfo ci) {
        FMLCommonHandler.instance().onRenderTickStart(this.timer.renderPartialTicks);
    }

    @Inject(method = "runGameLoop", at = @At(value = "INVOKE", target = "Lnet/minecraft/profiler/Profiler;endSection()V"
            , ordinal = 2, shift = At.Shift.AFTER))
    private void injectRenderTickEnd(CallbackInfo ci) {
        FMLCommonHandler.instance().onRenderTickEnd(this.timer.renderPartialTicks);
    }

    @Inject(method = "runTick", at = @At(value = "HEAD"))
    private void injectRescheduleClientTicks(CallbackInfo ci) {
        FMLCommonHandler.instance().rescheduleTicks(Side.CLIENT);
    }

    @Inject(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/profiler/Profiler;startSection(Ljava/lang/String;)V"
            , ordinal = 0, shift = At.Shift.BEFORE))
    private void injectPreClientTick(CallbackInfo ci) {
        FMLCommonHandler.instance().onPreClientTick();
    }

    @Inject(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/profiler/Profiler;endSection()V"
            , shift = At.Shift.BEFORE))
    private void injectPostClientTick(CallbackInfo ci) {
        FMLCommonHandler.instance().onPostClientTick();
    }

    @Inject(method = "launchIntegratedServer", at = @At(value = "NEW", target = "(Lnet/minecraft/client/Minecraft;Ljava/lang/String;Ljava/lang/String;Lnet/minecraft/world/WorldSettings;)Lnet/minecraft/huix.mixins.server/integrated/IntegratedServer;", shift = At.Shift.BEFORE))
    private void injectInitializeServerGate(CallbackInfo ci) {
        GameData.initializeServerGate(2);
    }

    @Inject(method = "launchIntegratedServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/integrated/IntegratedServer;startServerThread()V"
            , shift = At.Shift.AFTER))
    private void injectIdDifferences(CallbackInfo ci) {
        MapDifference<Integer, ItemData> idDifferences = GameData.gateWorldLoadingForValidation();
        if (idDifferences!=null)
        {
            FMLClientHandler.instance().warnIDMismatch(idDifferences, true);
        }
        else
        {
            GameData.releaseGate(true);
            /*
                public void continueWorldLoading()
    {
        this.field_71455_al = true;
        this.field_71461_s.func_73720_a(I18n.func_135053_a("menu.loadingLevel"));
             */
            this.integratedServerIsRunning = true;
            this.loadingScreen.displayProgressMessage(I18n.getString("menu.loadingLevel"));
        }
    }

}
