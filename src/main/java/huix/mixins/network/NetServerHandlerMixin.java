package huix.mixins.network;

import cpw.mods.fml.common.network.FMLNetworkHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemEditableBook;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemWritableBook;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.NetServerHandler;
import net.minecraft.network.packet.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.ChatMessageComponent;
import net.xiaoyu233.fml.util.ReflectHelper;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

@Mixin(NetServerHandler.class)
public class NetServerHandlerMixin extends NetHandler {


    @Inject(method = "handleChat", at = @At(value = "HEAD"), cancellable = true)
    private void injection_0(Packet3Chat par1Packet3Chat, CallbackInfo ci) {
        par1Packet3Chat = FMLNetworkHandler.handleChatMessage(ReflectHelper.dyCast(this), par1Packet3Chat);

        if (par1Packet3Chat == null || par1Packet3Chat.message == null) {
            ci.cancel();
        }
    }

    @Overwrite
    public void handleCustomPayload(Packet250CustomPayload par1Packet250CustomPayload) {
        FMLNetworkHandler.handlePacket250Packet(par1Packet250CustomPayload, this.getNetManager(), ReflectHelper.dyCast(this));
    }


    @Shadow
    public EntityPlayerMP playerEntity;
    @Shadow
    @Final
    private MinecraftServer mcServer;


    @Unique
    public void handleVanilla250Packet(Packet250CustomPayload par1Packet250CustomPayload) {
        DataInputStream var2;
        ItemStack var3;
        ItemStack var4;
        Exception var11;
        if ("MC|BEdit".equals(par1Packet250CustomPayload.channel)) {
            try {
                var2 = new DataInputStream(new ByteArrayInputStream(par1Packet250CustomPayload.data));
                var3 = Packet.readItemStack(var2);
                if (!ItemWritableBook.validBookTagPages(var3.getTagCompound())) {
                    throw new IOException("Invalid book tag!");
                }

                var4 = this.playerEntity.inventory.getCurrentItemStack();
                if (var3 != null && var3.itemID == Item.writableBook.itemID && var3.itemID == var4.itemID) {
                    var4.setTagInfo("pages", var3.getTagCompound().getTagList("pages"));
                }
            } catch (Exception var14) {
                var11 = var14;
                var11.printStackTrace();
            }
        } else if ("MC|BSign".equals(par1Packet250CustomPayload.channel)) {
            try {
                var2 = new DataInputStream(new ByteArrayInputStream(par1Packet250CustomPayload.data));
                var3 = Packet.readItemStack(var2);
                if (!ItemEditableBook.validBookTagContents(var3.getTagCompound())) {
                    throw new IOException("Invalid book tag!");
                }

                var4 = this.playerEntity.inventory.getCurrentItemStack();
                if (var3 != null && var3.itemID == Item.writtenBook.itemID && var4.itemID == Item.writableBook.itemID) {
                    var4.setTagInfo("author", new NBTTagString("author", this.playerEntity.getCommandSenderName()));
                    var4.setTagInfo("title", new NBTTagString("title", var3.getTagCompound().getString("title")));
                    var4.setTagInfo("pages", var3.getTagCompound().getTagList("pages"));
                    var4.itemID = Item.writtenBook.itemID;
                }
            } catch (Exception var13) {
                var11 = var13;
                var11.printStackTrace();
            }
        } else {
            int var14;
            if ("MC|TrSel".equals(par1Packet250CustomPayload.channel)) {
                try {
                    var2 = new DataInputStream(new ByteArrayInputStream(par1Packet250CustomPayload.data));
                    var14 = var2.readInt();
                    Container var16 = this.playerEntity.openContainer;
                    if (var16 instanceof ContainerMerchant) {
                        ((ContainerMerchant)var16).setCurrentRecipeIndex(var14);
                    }
                } catch (Exception var12) {
                    Exception var10 = var12;
                    var10.printStackTrace();
                }
            } else {
                Exception var9;
                String var15;
                int var18;
                if ("MC|AdvCdm".equals(par1Packet250CustomPayload.channel)) {
                    if (!this.mcServer.isCommandBlockEnabled()) {
                        this.playerEntity.sendChatToPlayer(ChatMessageComponent.createFromTranslationKey("advMode.notEnabled"));
                    } else if (this.playerEntity.canCommandSenderUseCommand(2, "") && this.playerEntity.capabilities.isCreativeMode) {
                        try {
                            var2 = new DataInputStream(new ByteArrayInputStream(par1Packet250CustomPayload.data));
                            var14 = var2.readInt();
                            var18 = var2.readInt();
                            int var5 = var2.readInt();
                            var15 = Packet.readString(var2, 256);
                            TileEntity var7 = this.playerEntity.worldObj.getBlockTileEntity(var14, var18, var5);
                            if (var7 != null && var7 instanceof TileEntityCommandBlock) {
                                ((TileEntityCommandBlock)var7).setCommand(var15);
                                this.playerEntity.worldObj.markBlockForUpdate(var14, var18, var5);
                                this.playerEntity.sendChatToPlayer(ChatMessageComponent.createFromTranslationWithSubstitutions("advMode.setCommand.success", new Object[]{var15}));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        this.playerEntity.sendChatToPlayer(ChatMessageComponent.createFromTranslationKey("advMode.notAllowed"));
                    }
                } else if ("MC|Beacon".equals(par1Packet250CustomPayload.channel)) {
                    if (this.playerEntity.openContainer instanceof ContainerBeacon) {
                        try {
                            var2 = new DataInputStream(new ByteArrayInputStream(par1Packet250CustomPayload.data));
                            var14 = var2.readInt();
                            var18 = var2.readInt();
                            ContainerBeacon var17 = (ContainerBeacon)this.playerEntity.openContainer;
                            Slot var19 = var17.getSlot(0);
                            if (var19.getHasStack()) {
                                var19.decrStackSize(1);
                                TileEntityBeacon var20 = var17.getBeacon();
                                var20.setPrimaryEffect(var14);
                                var20.setSecondaryEffect(var18);
                                var20.onInventoryChanged();
                            }
                        } catch (Exception var10) {
                            var9 = var10;
                            var9.printStackTrace();
                        }
                    }
                } else if ("MC|ItemName".equals(par1Packet250CustomPayload.channel) && this.playerEntity.openContainer instanceof ContainerRepair) {
                    ContainerRepair var13 = (ContainerRepair)this.playerEntity.openContainer;
                    if (par1Packet250CustomPayload.data != null && par1Packet250CustomPayload.data.length >= 1) {
                        var15 = ChatAllowedCharacters.filerAllowedCharacters(new String(par1Packet250CustomPayload.data));
                        if (var15.length() <= 30) {
                            var13.updateItemName(var15);
                        }
                    } else {
                        var13.updateItemName("");
                    }
                }
            }
        }

    }


    @Override
    public void handleMapData(Packet131MapData par1Packet131MapData) {
        FMLNetworkHandler.handlePacket131Packet(ReflectHelper.dyCast(this), par1Packet131MapData);
    }

    @Shadow
    public boolean isServerHandler() {
        return false;
    }

    @Shadow
    public INetworkManager getNetManager() {
        return null;
    }
}
