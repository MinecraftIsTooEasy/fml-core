package huix.mixins.client.multiplayer;

public class NetClientHandlerHelper {

    private static byte connectionCompatibilityLevel;

    public static void setConnectionCompatibilityLevel(byte connectionCompatibilityLevel) {
        NetClientHandlerHelper.connectionCompatibilityLevel = connectionCompatibilityLevel;
    }

    public static byte getConnectionCompatibilityLevel() {
        return connectionCompatibilityLevel;
    }
}
