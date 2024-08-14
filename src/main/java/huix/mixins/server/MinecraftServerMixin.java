package huix.mixins.server;


import com.llamalad7.mixinextras.sugar.Local;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.crash.CrashReport;
import net.minecraft.network.NetLoginHandler;
import net.minecraft.network.NetworkListenThread;
import net.minecraft.profiler.Profiler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.util.ReportedException;
import net.minecraft.world.WorldServer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.logging.Level;

@Mixin( MinecraftServer.class )
public abstract class MinecraftServerMixin {

    @Inject(method = "run", at =
            @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;startServer()Z", shift = At.Shift.AFTER))
    private void injection_0(CallbackInfo ci) {
        FMLCommonHandler.instance().handleServerStarted();
    }

    @Shadow
    public WorldServer[] worldServers;

    @Inject(method = "run", at =
            @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;getSystemTimeMillis()J", ordinal = 0, shift = At.Shift.AFTER))
    private void injection_1(CallbackInfo ci) {
        FMLCommonHandler.instance().onWorldLoadTick(worldServers);
    }

    @Inject(method = "run", at =
    @At(value = "INVOKE", target = "Ljava/lang/Thread;sleep(J)V", shift = At.Shift.AFTER))
    private void injection_2(CallbackInfo ci) {
        FMLCommonHandler.instance().handleServerStopping();
    }

    @Inject(method = "run", at =
    @At(value = "INVOKE", target = "Ljava/lang/Throwable;printStackTrace()V", shift = At.Shift.BEFORE), cancellable = true)
    private void injection_3(CallbackInfo ci) {
        if (FMLCommonHandler.instance().shouldServerBeKilledQuietly()) {
            ci.cancel();
        }
    }

    @Inject(method = "run", at =
    @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;stopServer()V", shift = At.Shift.BEFORE), cancellable = true)
    private void injection_4(CallbackInfo ci) {
        if (FMLCommonHandler.instance().shouldServerBeKilledQuietly()) {
            ci.cancel();
        }
    }

    @Shadow
    private boolean serverStopped;

    @Inject(method = "run", at =
    @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;systemExitNow()V", shift = At.Shift.BEFORE), cancellable = true)
    private void injection_5(CallbackInfo ci) {
        FMLCommonHandler.instance().handleServerStopped();
        this.serverStopped = true;
    }

    @Inject(method = "tick", at = @At(value = "HEAD"))
    private void injection_6(CallbackInfo ci) {
        FMLCommonHandler.instance().rescheduleTicks(Side.SERVER);
    }

    @Inject(method = "tick", at =
    @At(value = "INVOKE", target = "Lnet/minecraft/util/AABBPool;cleanPool()V", shift = At.Shift.AFTER))
    private void injection_7(CallbackInfo ci) {
        FMLCommonHandler.instance().onPreServerTick();
    }

    @Inject(method = "tick", at = @At(value = "RETURN"))
    private void injection_8(CallbackInfo ci) {
        FMLCommonHandler.instance().onPostServerTick();
    }

    @Shadow
    @Final
    public Profiler theProfiler;
    @Shadow
    protected int tickCounter;
    @Shadow
    public void sendWorldAgesToAllClientsInAllDimensions() {
    }
    @Shadow
    public boolean getAllowNether() {
        return false;
    }
    @Shadow
    @Final
    private List tickables;

    @Overwrite
    public void updateTimeLightAndEntities() {
        this.theProfiler.startSection("levels");
        if (this.tickCounter % 20 == 0) {
            this.sendWorldAgesToAllClientsInAllDimensions();
        }

        int var1;
        for(var1 = 0; var1 < this.worldServers.length; ++var1) {
            long var2 = System.nanoTime();
            if (var1 == 0 || this.getAllowNether()) {
                WorldServer var4 = this.worldServers[var1];
                this.theProfiler.startSection(var4.getWorldInfo().getWorldName());
                this.theProfiler.startSection("pools");
                var4.getWorldVec3Pool().clear();
                this.theProfiler.endSection();
                this.theProfiler.startSection("timeSync");
                this.theProfiler.endSection();
                this.theProfiler.startSection("tick");
                FMLCommonHandler.instance().onPreWorldTick(var4);

                CrashReport var6;
                Throwable var7;
                try {
                    var4.tick();
                } catch (Throwable var8) {
                    var7 = var8;
                    var6 = CrashReport.makeCrashReport(var7, "Exception ticking world");
                    var4.addWorldInfoToCrashReport(var6);
                    throw new ReportedException(var6);
                }

                try {
                    var4.updateEntities();
                } catch (Throwable throwable) {
                    var6 = CrashReport.makeCrashReport(throwable, "Exception ticking world entities");
                    var4.addWorldInfoToCrashReport(var6);
                    throw new ReportedException(var6);
                }

                this.theProfiler.endSection();

                FMLCommonHandler.instance().onPostWorldTick(var4);

                this.theProfiler.startSection("tracker");
                var4.getEntityTracker().updateTrackedEntities();
                this.theProfiler.endSection();
                this.theProfiler.endSection();
            }

            this.timeOfLastDimensionTick[var1][this.tickCounter % 100] = System.nanoTime() - var2;
        }

        this.theProfiler.endStartSection("connection");
        this.getNetworkThread().networkTick();
        this.theProfiler.endStartSection("players");
        this.serverConfigManager.sendPlayerInfoToAllPlayers(false);
        this.theProfiler.endStartSection("tickables");

        for(var1 = 0; var1 < this.tickables.size(); ++var1) {
            ((IUpdatePlayerListBox)this.tickables.get(var1)).update();
        }

        this.theProfiler.endSection();
    }

    @Shadow
    private ServerConfigurationManager serverConfigManager;
    @Shadow
    public long[][] timeOfLastDimensionTick;
    @Shadow
    public abstract NetworkListenThread getNetworkThread();

    @Overwrite
    public String getServerModName() {
        return FMLCommonHandler.instance().getModName();
    }
}
