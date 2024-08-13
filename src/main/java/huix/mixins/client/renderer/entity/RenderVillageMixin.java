package huix.mixins.client.renderer.entity;

import cpw.mods.fml.common.registry.VillagerRegistry;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderVillager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin( RenderVillager.class )
public class RenderVillageMixin extends RenderLiving {

    public RenderVillageMixin(ModelBase par1ModelBase, float par2) {
        super(par1ModelBase, par2);
    }

    @Overwrite
    protected ResourceLocation func_110902_a(EntityVillager par1EntityVillager) {
        return switch (par1EntityVillager.getProfession()) {
            case 0 -> this.textures[1];
            case 1 -> this.textures[2];
            case 2 -> this.textures[3];
            case 3 -> this.textures[4];
            case 4 -> this.textures[5];
            default -> VillagerRegistry.getVillagerSkin(par1EntityVillager.getProfession(), this.textures[0]);
        };
    }

    @Shadow
    protected void setTextures() {

    }

    @Shadow
    protected ResourceLocation getEntityTexture(Entity entity) {
        return null;
    }
}
