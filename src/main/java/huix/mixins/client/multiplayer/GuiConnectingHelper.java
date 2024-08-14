package huix.mixins.client.multiplayer;

import net.minecraft.client.multiplayer.GuiConnecting;

public class GuiConnectingHelper {

    public static void forceTermination(GuiConnecting gui) {
        gui.cancelled = true;
        gui.clientHandler = null;
    }
}
