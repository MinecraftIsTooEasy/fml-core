package huix.mixins.world.chunk.storage;

import huix.injected_interfaces.IIWorldInfo;
import net.minecraft.nbt.NBTBase;
import net.minecraft.world.storage.WorldInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.Map;

@Mixin( WorldInfo.class )
public class WorldInfoMixin implements IIWorldInfo {

    @Unique
    private Map<String,NBTBase> additionalProperties;

    @Unique
    @Override
    public void setAdditionalProperties(Map<String, NBTBase> paradditionalProperties) {
        this.additionalProperties = paradditionalProperties;
    }
}
