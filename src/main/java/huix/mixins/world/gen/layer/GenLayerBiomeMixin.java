package huix.mixins.world.gen.layer;

import huix.injected_interfaces.IIWorldType;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.GenLayerBiome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin (GenLayerBiome.class)
public class GenLayerBiomeMixin {

    @Shadow
    private BiomeGenBase[] allowedBiomes;

    @Inject(method = "<init>", at = @At(value = "RETURN"))
    private void injection(long par1, GenLayer par3GenLayer, WorldType par4WorldType, CallbackInfo ci) {
        this.allowedBiomes = ((IIWorldType) par4WorldType).getBiomesForWorldType();
    }

}
