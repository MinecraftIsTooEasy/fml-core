package huix.mixins.entity.player;

import cpw.mods.fml.common.network.Player;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin( EntityPlayerMP.class )
public class EntityPlayerMPMixin implements Player {


}
