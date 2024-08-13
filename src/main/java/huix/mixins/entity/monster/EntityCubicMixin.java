package huix.mixins.entity.monster;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityCubic;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin( EntityCubic.class )
public class EntityCubicMixin extends EntityLiving {

    public EntityCubicMixin(World par1World) {
        super(par1World);
    }

    @Overwrite
    public boolean getCanSpawnHere(boolean perform_light_check) {
        if (this.isGelatinousCube() && !this.isSlime()) {
            if (this.isAcidic() && this.getBlockBelow() != Block.stone) {
                return false;
            } else {
                return (!perform_light_check || this.isValidLightLevel()) && super.getCanSpawnHere(perform_light_check);
            }
        } else {
            Chunk var1 = this.worldObj.getChunkFromBlockCoords(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posZ));
            if (this.worldObj.getWorldInfo().getTerrainType().handleSlimeSpawnReduction(field_70146_Z, field_70170_p)) {
                return false;
            } else {
                if (this.getSize() == 1 || this.worldObj.difficultySetting > 0) {
                    BiomeGenBase var2 = this.worldObj.getBiomeGenForCoords(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posZ));
                    if (var2 == BiomeGenBase.swampland && this.posY > 50.0 && this.posY < 70.0 && this.rand.nextFloat() < 0.5F && this.rand.nextFloat() < this.worldObj.getCurrentMoonPhaseFactor() && this.worldObj.getBlockLightValue(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ)) <= this.rand.nextInt(8)) {
                        return super.getCanSpawnHere(perform_light_check);
                    }

                    if (this.rand.nextInt(10) == 0 && var1.getRandomWithSeed(987234911L).nextInt(10) == 0 && this.posY < 40.0) {
                        return super.getCanSpawnHere(perform_light_check);
                    }
                }

                return false;
            }
        }
    }

    @Shadow
    @Final
    public boolean isSlime() {
        return true;
    }
    @Shadow
    public int getSize() {
        return 1;
    }
    @Shadow
    @Final
    public boolean isGelatinousCube() {
        return false;
    }
    @Shadow
    @Final
    public final boolean isAcidic() {
        return false;
    }
}
