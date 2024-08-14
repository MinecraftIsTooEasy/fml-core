package huix.mixins.client.multiplayer;


import com.google.common.base.Charsets;
import cpw.mods.fml.common.network.FMLNetworkHandler;
import huix.injected_interfaces.IINetClientHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMerchant;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.NetClientHandler;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMap;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.*;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.village.MerchantRecipeList;
import net.xiaoyu233.fml.util.ReflectHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

@Mixin( NetClientHandler.class )
public class NetClientHandlerMixin implements IINetClientHandler {

    @Shadow
    private Minecraft mc;
    @Shadow
    private INetworkManager netManager;


    @Inject(method = "<init>(Lnet/minecraft/client/Minecraft;Ljava/lang/String;I)V", at = @At("RETURN"))
    private void injectInit_0(Minecraft par1Minecraft, String par2Str, int par3, CallbackInfo ci) {
        FMLNetworkHandler.onClientConnectionToRemoteServer(ReflectHelper.dyCast(this), par2Str, par3, this.netManager);
    }

    @Inject(method = "<init>(Lnet/minecraft/client/Minecraft;Ljava/lang/String;ILnet/minecraft/client/gui/GuiScreen;)V", at = @At("RETURN"))
    private void injectInit_2(Minecraft par1Minecraft, String par2Str, int par3, GuiScreen par4GuiScreen, CallbackInfo ci) {
        FMLNetworkHandler.onClientConnectionToRemoteServer(ReflectHelper.dyCast(this), par2Str, par3, this.netManager);
    }

    @Inject(method = "<init>(Lnet/minecraft/client/Minecraft;Lnet/minecraft/server/integrated/IntegratedServer;)V", at = @At("RETURN"))
    private void injectInit_2(Minecraft par1Minecraft, IntegratedServer par2IntegratedServer, CallbackInfo ci) {
        FMLNetworkHandler.onClientConnectionToIntegratedServer(ReflectHelper.dyCast(this), par2IntegratedServer, this.netManager);
    }


    @Shadow
    public void addToSendQueue(Packet par1Packet) {
    }

    @Inject(method = "handleSharedKey", at = @At("HEAD"))
    private void injectHandleSharedKey(Packet252SharedKey par1Packet252SharedKey, CallbackInfo ci) {
        this.addToSendQueue(FMLNetworkHandler.getFMLFakeLoginPacket());
    }

    @Inject(method = "handleLogin", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/settings/GameSettings;sendSettingsToServer()V"
            , shift = At.Shift.BEFORE))
    private void injectHandleLogin(Packet1Login par1Packet1Login, CallbackInfo ci) {
        FMLNetworkHandler.onConnectionEstablishedToServer(ReflectHelper.dyCast(this), this.netManager, par1Packet1Login);
    }

    @Inject(method = "quitWithPacket", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/INetworkManager;serverShutdown()V"
            , shift = At.Shift.AFTER))
    private void injectQuitWithPacket(Packet par1Packet, CallbackInfo ci) {
        FMLNetworkHandler.onConnectionClosed(this.netManager, this.getPlayer());
    }

    @Inject(method = "handleChat", at = @At(value = "HEAD"), cancellable = true)
    private void injectHandleChat(Packet3Chat packet3Chat, CallbackInfo ci) {
        packet3Chat = FMLNetworkHandler.handleChatMessage(ReflectHelper.dyCast(this), packet3Chat);
        if (packet3Chat == null)
        {
            ci.cancel();
        }

        this.mc.ingameGUI.getChatGUI().printChatMessage(ChatMessageComponent.createFromJson(packet3Chat.message).toStringWithFormatting(true));
        ci.cancel();
    }

    @Inject(method = "handleMapData", at = @At(value = "HEAD"), cancellable = true)
    private void injectHandleMapData(Packet131MapData par1Packet131MapData, CallbackInfo ci) {
        FMLNetworkHandler.handlePacket131Packet(ReflectHelper.dyCast(this), par1Packet131MapData);
        ci.cancel();
    }

    @Unique
    @Override
    public void fmlPacket131Callback(Packet131MapData par1Packet131MapData) {
        if (par1Packet131MapData.itemID == Item.map.itemID) {
            ItemMap.getMPMapData(par1Packet131MapData.uniqueID, this.mc.theWorld).updateMPMapData(par1Packet131MapData.itemData);
        } else {
            this.mc.getLogAgent().logWarning("Unknown itemid: " + par1Packet131MapData.uniqueID);
        }

    }

    @Inject(method = "handleCustomPayload", at = @At(value = "HEAD"), cancellable = true)
    private void injectHandleCustomPayload(Packet250CustomPayload par1Packet250CustomPayload, CallbackInfo ci) {
        FMLNetworkHandler.handlePacket250Packet(par1Packet250CustomPayload, this.netManager, ReflectHelper.dyCast(this));
        ci.cancel();
    }

    @Unique
    @Override
    public void handleVanilla250Packet(Packet250CustomPayload par1Packet250CustomPayload) {
        if ("MC|TrList".equals(par1Packet250CustomPayload.channel)) {
            DataInputStream var2 = new DataInputStream(new ByteArrayInputStream(par1Packet250CustomPayload.data));

            try {
                int var3 = var2.readInt();
                GuiScreen var4 = this.mc.currentScreen;
                if (var4 != null && var4 instanceof GuiMerchant && var3 == this.mc.thePlayer.openContainer.windowId) {
                    IMerchant var5 = ((GuiMerchant)var4).getIMerchant();
                    MerchantRecipeList var6 = MerchantRecipeList.readRecipiesFromStream(var2);
                    var5.setRecipes(var6);
                }
            } catch (IOException var7) {
                var7.printStackTrace();
            }
        } else if ("MC|Brand".equals(par1Packet250CustomPayload.channel)) {
            this.mc.thePlayer.func_142020_c(new String(par1Packet250CustomPayload.data, Charsets.UTF_8));
        }
    }

    @Unique
    @Override
    public EntityPlayer getPlayer() {
        return this.mc.thePlayer;
    }


}
