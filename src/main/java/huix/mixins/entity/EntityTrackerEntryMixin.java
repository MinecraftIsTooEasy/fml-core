package huix.mixins.entity;


import cpw.mods.fml.common.network.FMLNetworkHandler;
import cpw.mods.fml.common.registry.EntityRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityTrackerEntry;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.packet.Packet;
import net.xiaoyu233.fml.util.ReflectHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.util.MathHelper;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin( EntityTrackerEntry.class )
public class EntityTrackerEntryMixin {

    @Shadow
    public Entity myEntity;
    @Shadow
    public int lastScaledXPosition;
    @Shadow
    public int lastScaledYPosition;
    @Shadow
    public int lastScaledZPosition;

    @Inject(method = "tryStartWachingThis", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/DataWatcher;getIsBlank()Z"))
    private void injectMakeEntitySpawnAdjustment(EntityPlayerMP par1EntityPlayerMP, CallbackInfo ci)   {
        int posX = MathHelper.floor_double(this.myEntity.posX * 32.0D);
        int posY = MathHelper.floor_double(this.myEntity.posY * 32.0D);
        int posZ = MathHelper.floor_double(this.myEntity.posZ * 32.0D);
        if (posX != this.lastScaledXPosition || posY != this.lastScaledYPosition || posZ != this.lastScaledZPosition)
        {
            FMLNetworkHandler.makeEntitySpawnAdjustment(this.myEntity.entityId, par1EntityPlayerMP, this.lastScaledXPosition, this.lastScaledYPosition, this.lastScaledZPosition);
        }
    }

    @Inject(method = "getPacketForThisEntity", at = @At(value = "HEAD"), cancellable = true)
    private void injectMakeEntitySpawnAdjustment(CallbackInfoReturnable<Packet> cir)   {
        Packet pkt = FMLNetworkHandler.getEntitySpawningPacket(this.myEntity);

        if (pkt != null) {
             cir.setReturnValue(pkt);;
        }
    }



}
