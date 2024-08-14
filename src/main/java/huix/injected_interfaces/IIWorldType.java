package huix.injected_interfaces;


import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.chunk.IChunkProvider;

import java.util.Random;

public interface IIWorldType {

    default WorldChunkManager getChunkManager(World world) {
        return null;
    }

    default IChunkProvider getChunkGenerator(World world, String generatorOptions)
    {
        return null;
    }

    default int getMinimumSpawnHeight(World world)
    {
        return 0;
    }

    default double getHorizon(World world)
    {
        return 0;
    }

    default boolean hasVoidParticles(boolean flag)
    {
        return false;
    }

    default double voidFadeMagnitude()
    {
        return 0;
    }

    default BiomeGenBase[] getBiomesForWorldType()
    {
        return null;
    }

    default void addNewBiome(BiomeGenBase biome)
    {

    }

    default void removeBiome(BiomeGenBase biome)
    {

    }

    default boolean handleSlimeSpawnReduction(Random random, World world)
    {
        return false;
    }

    default void onGUICreateWorldPress() { }
}
