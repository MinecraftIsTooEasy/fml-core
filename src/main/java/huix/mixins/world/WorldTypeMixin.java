package huix.mixins.world;


import huix.injected_interfaces.IIWorldType;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;
import net.xiaoyu233.fml.util.ReflectHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;
import java.util.Random;
import java.util.Set;

import com.google.common.collect.Sets;

import net.minecraft.world.*;
import net.minecraft.world.gen.*;
import net.minecraft.world.biome.*;
import net.minecraft.world.chunk.IChunkProvider;

@Mixin( WorldType.class )
public class WorldTypeMixin implements IIWorldType {


    @Unique
    protected BiomeGenBase[] biomesForWorldType;

    @Inject(method = "<init>(ILjava/lang/String;I)V", at = @At(value = "RETURN"))
    private void injection(int i, String string, int j, CallbackInfo ci) {
        if (i == 8) {
            biomesForWorldType = WorldTypeHelper.base11Biomes;
        } else {
            biomesForWorldType = WorldTypeHelper.base12Biomes;
        }
    }


    @Shadow
    @Final
    public static WorldType FLAT;

    @Unique
    @Override
    public WorldChunkManager getChunkManager(World world)
    {
        if (ReflectHelper.dyCast(ReflectHelper.dyCast(this)) == FLAT)
        {
            FlatGeneratorInfo flatgeneratorinfo = FlatGeneratorInfo.createFlatGeneratorFromString(world.getWorldInfo().getGeneratorOptions());
            return new WorldChunkManagerHell(BiomeGenBase.biomeList[flatgeneratorinfo.getBiome()], 0.5F, 0.5F);
        }
        else
        {
            return new WorldChunkManager(world);
        }
    }

    @Unique
    @Override
    public IChunkProvider getChunkGenerator(World world, String generatorOptions)
    {
        return ReflectHelper.dyCast(this) == WorldType.FLAT ?
                new ChunkProviderFlat(world, world.getSeed(), world.getWorldInfo().isMapFeaturesEnabled(), generatorOptions) :
                new ChunkProviderGenerate(world, world.getSeed(), world.getWorldInfo().isMapFeaturesEnabled());
    }

    @Unique
    @Override
    public int getMinimumSpawnHeight(World world)
    {
        return ReflectHelper.dyCast(this) == FLAT ? 4 : 64;
    }

    @Unique
    @Override
    public double getHorizon(World world)
    {
        return ReflectHelper.dyCast(this) == FLAT ? 0.0D : 63.0D;
    }

    @Unique
    @Override
    public boolean hasVoidParticles(boolean flag)
    {
        return ReflectHelper.dyCast(this) != FLAT && !flag;
    }

    @Unique
    @Override
    public double voidFadeMagnitude()
    {
        return ReflectHelper.dyCast(this) == FLAT ? 1.0D : 0.03125D;
    }

    @Unique
    @Override
    public BiomeGenBase[] getBiomesForWorldType()
    {
        return biomesForWorldType;
    }

    @Unique
    @Override
    public void addNewBiome(BiomeGenBase biome)
    {
        Set<BiomeGenBase> newBiomesForWorld = Sets.newLinkedHashSet(Arrays.asList(biomesForWorldType));
        newBiomesForWorld.add(biome);
        biomesForWorldType = newBiomesForWorld.toArray(new BiomeGenBase[0]);
    }

    @Unique
    @Override
    public void removeBiome(BiomeGenBase biome)
    {
        Set<BiomeGenBase> newBiomesForWorld = Sets.newLinkedHashSet(Arrays.asList(biomesForWorldType));
        newBiomesForWorld.remove(biome);
        biomesForWorldType = newBiomesForWorld.toArray(new BiomeGenBase[0]);
    }

    @Unique
    @Override
    public boolean handleSlimeSpawnReduction(Random random, World world)
    {
        return ReflectHelper.dyCast(this) == FLAT ? random.nextInt(4) != 1 : false;
    }

    /**
     * Called when 'Create New World' button is pressed before starting game
     */
    @Unique
    @Override
    public void onGUICreateWorldPress() { }

}
