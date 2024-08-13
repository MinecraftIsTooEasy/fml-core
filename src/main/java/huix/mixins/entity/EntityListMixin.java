package huix.mixins.entity;

import cpw.mods.fml.common.FMLLog;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;
import java.util.logging.Level;

@Mixin( EntityList.class )
public class EntityListMixin {


    @Overwrite
    public static Entity createEntityByID(int par0, World par1World) {
        Entity var2 = null;

        try {
            Class var3 = getClassFromID(par0);
            if (var3 != null) {
                try
                {
                    var2 = (Entity)var3.getConstructor(World.class).newInstance(par1World);
                }
                catch (Exception e)
                {
                    FMLLog.log(Level.SEVERE, e,
                            "An Entity %s(%s) has thrown an exception during loading, its state cannot be restored. Report this to the mod author",
                            par0, var3.getName());
                    var2 = null;
                }

            }
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        if (var2 == null) {
            par1World.getWorldLogAgent().logWarning("Skipping Entity with id " + par0);
        }

        return var2;
    }

    @Shadow
    public static Class getClassFromID(int par0) {
        return null;
    }

}
