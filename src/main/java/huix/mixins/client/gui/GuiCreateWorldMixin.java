package huix.mixins.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiCreateWorld;
import net.minecraft.client.resources.ResourceManager;
import net.minecraft.world.WorldType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin( GuiCreateWorld.class )
public class GuiCreateWorldMixin {


    @Shadow
    private int worldTypeId;

    @Inject(method = "actionPerformed", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/EnumGameType;getByName(Ljava/lang/String;)Lnet/minecraft/world/EnumGameType;", shift = At.Shift.BEFORE))
    private void injectActionPerformed(GuiButton par1GuiButton, CallbackInfo ci) {
        WorldType.worldTypes[this.worldTypeId].onGUICreateWorldPress();
    }
}
