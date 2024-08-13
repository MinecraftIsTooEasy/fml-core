package huix.mixins.entity;

import cpw.mods.fml.common.registry.EntityRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityTracker;
import net.xiaoyu233.fml.util.ReflectHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin( EntityTracker.class )
public class EntityTrackerMixin {

    @Inject(method = "addEntityToTracker(Lnet/minecraft/entity/Entity;)V", at = @At(value = "HEAD"), cancellable = true)
    private void injectTrackering(Entity par1Entity, CallbackInfo ci)   {
        if (EntityRegistry.instance().tryTrackingEntity(ReflectHelper.dyCast(this), par1Entity)) {
            ci.cancel();
        }
    }

}
