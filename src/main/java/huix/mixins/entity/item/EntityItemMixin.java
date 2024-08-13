package huix.mixins.entity.item;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.xiaoyu233.fml.util.ReflectHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin( EntityItem.class )
public class EntityItemMixin {

    @Inject(method = "onCollideWithPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/item/EntityItem;playSound(Ljava/lang/String;FF)V"
            , shift = At.Shift.BEFORE))
    private void injectRenderTickStart(EntityPlayer par1EntityPlayer, CallbackInfo ci) {
        GameRegistry.onPickupNotification(par1EntityPlayer, ReflectHelper.dyCast(this));
    }
}
