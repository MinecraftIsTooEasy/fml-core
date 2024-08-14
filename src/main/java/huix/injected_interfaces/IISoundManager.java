package huix.injected_interfaces;

public interface IISoundManager {
    default boolean getLOAD_SOUND_SYSTEM() {
        return false;
    }

    default void setLOAD_SOUND_SYSTEM(boolean value) {

    }
}
