package huix.mixins.world;

import com.google.common.collect.ObjectArrays;
import net.minecraft.world.biome.BiomeGenBase;

public class WorldTypeHelper {
    public static final BiomeGenBase[] base11Biomes =
            new BiomeGenBase[] {BiomeGenBase.ocean, BiomeGenBase.desert, BiomeGenBase.forest, BiomeGenBase.swampland,
                    BiomeGenBase.plains, BiomeGenBase.taiga};
    public static final BiomeGenBase[] base12Biomes =
            ObjectArrays.concat(base11Biomes, BiomeGenBase.jungle);
}
