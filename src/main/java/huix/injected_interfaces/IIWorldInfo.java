package huix.injected_interfaces;

import net.minecraft.nbt.NBTBase;

import java.util.Map;

public interface IIWorldInfo {

    default void setAdditionalProperties(Map<String, NBTBase> additionalProperties) {

    }
}
