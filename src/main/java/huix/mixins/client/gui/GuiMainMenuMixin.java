package huix.mixins.client.gui;


import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.llamalad7.mixinextras.sugar.Local;
import cpw.mods.fml.client.GuiModList;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.resources.I18n;
import net.minecraft.world.WorldType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin( GuiMainMenu.class )
public class GuiMainMenuMixin extends GuiScreen {

    @Unique
    private GuiButton fmlModButton = null;


    @Inject(method = "func_130022_h", at = @At(value = "RETURN"))
    private void injectActionPerformed(CallbackInfo ci) {
        fmlModButton.xPosition = 98;
        fmlModButton.yPosition = this.width / 2 + 2;
    }

    @Shadow
    private GuiButton minecraftRealmsButton;

    @Inject(method = "addSingleplayerMultiplayerButtons", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/GuiButton;drawButton:Z"
            , ordinal = 1, shift = At.Shift.BEFORE))
    private void injectAddSingleplayerMultiplayerButtons(CallbackInfo ci, @Local(ordinal = 0, argsOnly = true) int p_73969_1_,
                                                         @Local(ordinal = 1, argsOnly = true) int p_73969_2_) {
        //If Minecraft Realms is enabled, halve the size of both buttons and set them next to eachother.
        fmlModButton = new GuiButton(6, this.width / 2 - 100, p_73969_1_ + p_73969_2_ * 2, "Mods");
        this.buttonList.add(fmlModButton);

        minecraftRealmsButton = new GuiButton(14, this.width / 2 - 100, p_73969_1_ + p_73969_2_ * 2, I18n.getString("menu.online"));
        minecraftRealmsButton.xPosition = 98;
        minecraftRealmsButton.yPosition = this.width / 2 - 100;
        this.buttonList.add(minecraftRealmsButton);
    }

    @Inject(method = "actionPerformed", at = @At(value = "INVOKE", target = "Ljava/lang/Class;forName(Ljava/lang/String;)Ljava/lang/Class;", shift = At.Shift.BEFORE))
    private void injectActionPerformed_(CallbackInfo ci) {
        this.mc.displayGuiScreen(new GuiModList(this));
    }

    @Inject(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiMainMenu;drawString(Lnet/minecraft/client/gui/FontRenderer;Ljava/lang/String;III)V"
            , ordinal = 0, shift = At.Shift.AFTER))
    private void injectDrawScreen(CallbackInfo ci) {
        List<String> brandings = Lists.reverse(FMLCommonHandler.instance().getBrandings());
        for (int i = 0; i < brandings.size(); i++)
        {
            String brd = brandings.get(i);
            if (!Strings.isNullOrEmpty(brd))
            {
                this.drawString(this.fontRenderer, brd, 2, this.height - ( 10 + i * (this.fontRenderer.FONT_HEIGHT + 1)), 16777215);
            }
        }
    }

}
