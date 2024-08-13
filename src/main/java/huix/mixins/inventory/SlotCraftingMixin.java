package huix.mixins.inventory;

import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;
import net.xiaoyu233.fml.util.ReflectHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin( SlotCrafting.class)
public class SlotCraftingMixin {

    @Shadow
    @Final
    private IInventory craftMatrix;

    @Inject(method = "onPickupFromSlot", at = @At(value = "HEAD"))
    private void injectOnPickupFromSlot(EntityPlayer par1EntityPlayer, ItemStack par2ItemStack, CallbackInfo ci)   {
        GameRegistry.onItemCrafted(par1EntityPlayer, par2ItemStack, craftMatrix);
    }

}
