package huix.mixins.client;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.client.ClientBrandRetriever;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin( ClientBrandRetriever.class )
public class ClientBrandRetrieverMixin {

    @Overwrite
    public static String getClientModName() {
        return FMLCommonHandler.instance().getModName();
    }
}
