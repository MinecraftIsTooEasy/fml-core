package huix.mixins.world;

import huix.injected_interfaces.IIWorldType;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.chunk.IChunkProvider;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin( WorldProvider.class)
public class WorldProviderMixin {
    @Shadow
    public World worldObj;
    @Shadow
    public WorldType terrainType;
    @Shadow
    public WorldChunkManager worldChunkMgr;

    @Overwrite
    protected void registerWorldChunkManager() {
        this.worldChunkMgr =((IIWorldType) this.terrainType).getChunkManager(this.worldObj);
    }

    @Overwrite
    public int getAverageGroundLevel() {
        return ((IIWorldType) this.terrainType).getMinimumSpawnHeight(this.worldObj);
    }

    @Shadow
    public String field_82913_c;
    @Overwrite
    public IChunkProvider createChunkGenerator() {
        return ((IIWorldType) this.terrainType).getChunkGenerator(this.worldObj, field_82913_c);
    }

    @Overwrite
    public double getVoidFogYFactor() {
        return ((IIWorldType) this.terrainType).voidFadeMagnitude();
    }

    @Shadow
    @Final
    public boolean is_the_end;

    @Overwrite
    public boolean getWorldHasVoidParticles() {
        return ((IIWorldType) this.terrainType).hasVoidParticles(this.is_the_end);
    }





}
