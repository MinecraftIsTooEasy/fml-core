package huix.mixins.server.management;

import com.llamalad7.mixinextras.sugar.Local;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.FMLNetworkHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.NetServerHandler;
import net.minecraft.server.management.ServerConfigurationManager;
import net.xiaoyu233.fml.util.ReflectHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin( ServerConfigurationManager.class )
public class ServerConfigurationManagerMixin {

    @Inject(method = "initializeConnectionToPlayer",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/EntityPlayerMP;addSelfToInternalCraftingInventory()V"
            , shift = At.Shift.AFTER))
    private void injection_0(INetworkManager par1INetworkManager, EntityPlayerMP par2EntityPlayerMP, CallbackInfo ci, @Local NetServerHandler netServerHandler) {
        FMLNetworkHandler.handlePlayerLogin(par2EntityPlayerMP, netServerHandler, par1INetworkManager);
    }

    @Inject(method = "playerLoggedOut",
            at = @At(value = "HEAD"))
    private void injection_1(EntityPlayerMP par1EntityPlayerMP, CallbackInfo ci) {
        GameRegistry.onPlayerLogout(par1EntityPlayerMP);
    }

    @Inject(method = "respawnPlayer", at = @At(value = "RETURN"))
    private void injection_2(EntityPlayerMP par1EntityPlayerMP, int par2, boolean par3, CallbackInfoReturnable<EntityPlayerMP> cir
            , @Local(ordinal = 1) EntityPlayerMP entityPlayerMP) {
        GameRegistry.onPlayerRespawn(entityPlayerMP);
    }

    @Inject(method = "transferPlayerToDimension", at = @At(value = "RETURN"))
    private void injection_3(EntityPlayerMP par1EntityPlayerMP, int par2, CallbackInfo ci) {
        GameRegistry.onPlayerChangedDimension(par1EntityPlayerMP);
    }
}
