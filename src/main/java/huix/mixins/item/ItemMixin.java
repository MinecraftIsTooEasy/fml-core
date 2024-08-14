package huix.mixins.item;

import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemEditableBook;
import net.minecraft.item.ItemStack;
import net.xiaoyu233.fml.util.ReflectHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin( Item.class )
public class ItemMixin {

    @Shadow public static Item itemFrame;


    @Inject(method = "<init>(ILjava/lang/String;I)V", at = @At(value = "RETURN"))
    private void injectItemAdd(int par1, String texture, int num_subtypes, CallbackInfo ci)   {
        GameData.newItemAdded(ReflectHelper.dyCast(this));
    }

    @Overwrite
    public int getBurnTime(ItemStack item_stack) {
        if (ReflectHelper.dyCast(this) == Item.paper) {
            return 25;
        } else if (ReflectHelper.dyCast(this) == Item.manure) {
            return 100;
        } else if (ReflectHelper.dyCast(this) != Item.stick && !(ReflectHelper.dyCast(this) instanceof ItemArrow)) {
            if (ReflectHelper.dyCast(this) != Item.book && ReflectHelper.dyCast(this) != Item.writableBook
                    && !(ReflectHelper.dyCast(this) instanceof ItemEditableBook) && ReflectHelper.dyCast(this) != Item.enchantedBook) {
                if (ReflectHelper.dyCast(this) == Item.doorWood) {
                    return 400;
                } else if (ReflectHelper.dyCast(this) == Item.blazeRod) {
                    return 2400;
                } else if (this.hasMaterial(Material.wood)) {
                    return 200;
                } else {
                    return this.hasMaterial(Material.paper) ? 50 : GameRegistry.getFuelValue(item_stack);
                }
            } else {
                return GameRegistry.getFuelValue(item_stack);
            }
        } else {
            return GameRegistry.getFuelValue(item_stack);
        }
    }

    @Shadow
    public boolean hasMaterial(Material material) {
        return false;
    }

}
