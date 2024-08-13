package huix.mixins.entity.passive;

import com.llamalad7.mixinextras.sugar.Local;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.registry.VillagerRegistry;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.village.MerchantRecipeList;
import net.xiaoyu233.fml.util.ReflectHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin( EntityVillager.class )
public class EntityVillagerMixin {
    @Shadow
    private int randomTickDivider;

    @Inject(method = "addDefaultEquipmentAndRecipies", at = @At(value = "NEW", target = "()Lnet/minecraft/village/MerchantRecipeList;", ordinal = 0, shift = At.Shift.AFTER))
    private void injectAddDefaultEquipmentAndRecipies(CallbackInfo ci, @Local(ordinal = 0) MerchantRecipeList merchantRecipeList) {
        //need fix
        VillagerRegistry.manageVillagerTrades(merchantrecipelist, ReflectHelper.dyCast(this), this.getProfession(), this.randomTickDivider);
    }

    @Shadow
    public int getProfession() {
        return 1;
    }



}
