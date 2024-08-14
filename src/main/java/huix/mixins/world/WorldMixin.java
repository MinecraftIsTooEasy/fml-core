package huix.mixins.world;

import huix.injected_interfaces.IIWorldType;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldInfo;
import net.xiaoyu233.fml.util.ReflectHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin( World.class )
public class WorldMixin {

    @Shadow
    protected WorldInfo worldInfo;

    @Overwrite
    public double getHorizon() {
        return ((IIWorldType) this.worldInfo.getTerrainType()).getHorizon(ReflectHelper.dyCast(this));
    }

}
