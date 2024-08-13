package huix.mixins.inventory;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin( SlotFurnace.class )
public class SlotFurnaceMixin {

    @Inject(method = "onPickupFromSlot", at = @At(value = "HEAD"))
    private void injectOnPickupFromSlot(EntityPlayer par1EntityPlayer, ItemStack par2ItemStack, CallbackInfo ci)   {
        GameRegistry.onItemSmelted(par1EntityPlayer, par2ItemStack);
    }

}
