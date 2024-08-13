package huix.mixins.tileentity;

import cpw.mods.fml.common.FMLLog;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;
import java.util.logging.Level;

@Mixin( TileEntity.class )
public class TileEntityMixin {

    @Shadow
    private static Map nameToClassMap;

    @Overwrite
    public static TileEntity createAndLoadEntity(NBTTagCompound par0NBTTagCompound) {
        TileEntity var1 = null;

        try {
            Class var2 = (Class)nameToClassMap.get(par0NBTTagCompound.getString("id"));
            if (var2 != null) {

                try {
                    var1 = (TileEntity)var2.newInstance();
                } catch (Exception e) {
                    FMLLog.log(Level.SEVERE, e,
                            "A TileEntity %s(%s) has thrown an exception during loading, its state cannot be restored. Report this to the mod author",
                            par0NBTTagCompound.getString("id"), var2.getName());
                }

            }
        } catch (Exception var3) {
            var3.printStackTrace();
        }

        if (var1 != null) {
            var1.readFromNBT(par0NBTTagCompound);
        } else {
            MinecraftServer.getServer().getLogAgent().logWarning("Skipping TileEntity with id " + par0NBTTagCompound.getString("id"));
        }

        return var1;
    }

}
