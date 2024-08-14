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

import java.util.Random;

@Mixin( EntityVillager.class )
public class EntityVillagerMixin {
    @Shadow
    private int randomTickDivider;

    private Random random = new Random();

    @Inject(method = "addDefaultEquipmentAndRecipies", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/passive/EntityVillager;getProfession()I", shift = At.Shift.BEFORE))
    private void injectAddDefaultEquipmentAndRecipies(CallbackInfo ci, @Local MerchantRecipeList merchantRecipeList) {
        VillagerRegistry.manageVillagerTrades(merchantRecipeList, ReflectHelper.dyCast(this), this.getProfession(), this.random);
    }

    @Shadow
    public int getProfession() {
        return 1;
    }



}
