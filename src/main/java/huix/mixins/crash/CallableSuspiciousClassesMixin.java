package huix.mixins.crash;

import net.minecraft.crash.CallableSuspiciousClasses;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin( CallableSuspiciousClasses.class )
public class CallableSuspiciousClassesMixin {

    @Unique
    public Object call() {
        return "FML is installed";
    }
}
