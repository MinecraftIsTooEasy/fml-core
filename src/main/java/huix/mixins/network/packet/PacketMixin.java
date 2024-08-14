package huix.mixins.network.packet;

import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet131MapData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin( Packet.class )
public class PacketMixin {

    @Inject(method = "<clinit>", at = @At("RETURN"))
    private static void injectClinit(CallbackInfo ci) {
        addIdClassMapping(131, true, true, Packet131MapData.class);
    }

    @Shadow
    static void addIdClassMapping(int par0, boolean par1, boolean par2, Class par3Class) {
    }

}
