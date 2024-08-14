package huix.mixins.client.multiplayer;

import huix.injected_interfaces.IIGuiConnecting;
import net.minecraft.client.multiplayer.GuiConnecting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin( GuiConnecting.class )
public class GuiConnectingMixin implements IIGuiConnecting {

    @Unique
    public void forceTermination(GuiConnecting gui) {
        gui.cancelled = true;
        gui.clientHandler = null;
    }

}
