package huix.mixins.entity.player;

import com.llamalad7.mixinextras.sugar.Local;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.FMLNetworkHandler;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.common.registry.VillagerRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.World;
import net.xiaoyu233.fml.util.ReflectHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin( EntityPlayer.class )
public class EntityPlayerMixin implements Player {

    @Inject(method = "onLivingUpdate", at = @At(value = "HEAD"))
    private void injectPlayerPreTick(CallbackInfo ci)   {
        FMLCommonHandler.instance().onPlayerPreTick(ReflectHelper.dyCast(this));
    }

    @Inject(method = "onLivingUpdate", at = @At(value = "RETURN"))
    private void injectPlayerPostTick(CallbackInfo ci)   {
        FMLCommonHandler.instance().onPlayerPostTick(ReflectHelper.dyCast(this));
    }

    @Unique
    @Override
    public void openGui(Object mod, int modGuiId, World world, int x, int y, int z) {
        FMLNetworkHandler.openGui(ReflectHelper.dyCast(this), mod, modGuiId, world, x, y, z);
    }
}
