package huix.mixins.world.gen;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin( ChunkProviderServer.class )
public class ChunkProviderServerMixin {

    @Shadow
    private IChunkProvider currentChunkProvider;
    @Shadow
    public WorldServer worldObj;

    @Overwrite
    public void populate(IChunkProvider par1IChunkProvider, int par2, int par3) {
        Chunk var4 = this.provideChunk(par2, par3);
        if (!var4.isTerrainPopulated) {
            var4.isTerrainPopulated = true;
            if (this.currentChunkProvider != null) {
                this.currentChunkProvider.populate(par1IChunkProvider, par2, par3);
                GameRegistry.generateWorld(par2, par3, worldObj, currentChunkProvider, par1IChunkProvider);
                var4.setChunkModified();
            }
        }

    }

    @Shadow
    public Chunk provideChunk(int par1, int par2) {
        return null;
    }
}
