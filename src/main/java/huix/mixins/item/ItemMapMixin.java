package huix.mixins.item;

import com.llamalad7.mixinextras.sugar.Local;
import cpw.mods.fml.common.registry.GameData;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapData;
import net.xiaoyu233.fml.util.ReflectHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin( ItemMap.class )
public class ItemMapMixin {

//    @Inject(method = "getMapData", at = @At(value = "FIELD", target = "Lnet/minecraft/world/storage/MapData;dimension:B"))
//    private void injectionForge(ItemStack par1ItemStack, World par2World, CallbackInfoReturnable<MapData> cir, @Local()MapData mapData)   {
//        mapData.dimension = par2World.provider.dimensionId;
//    }

}
