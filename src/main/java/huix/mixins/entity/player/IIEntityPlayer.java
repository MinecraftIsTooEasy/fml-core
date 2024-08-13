package huix.mixins.entity.player;

import net.minecraft.world.World;

public interface IIEntityPlayer {
    default void openGui(Object mod, int modGuiId, World world, int x, int y, int z) {

    }
}
