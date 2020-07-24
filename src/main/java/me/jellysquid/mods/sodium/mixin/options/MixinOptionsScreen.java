package me.jellysquid.mods.sodium.mixin.options;

import me.jellysquid.mods.sodium.client.gui.SodiumOptionsGUI;
import net.minecraft.client.GameSettings;
import net.minecraft.client.gui.screen.OptionsScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.VideoSettingsScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OptionsScreen.class)
public class MixinOptionsScreen extends Screen {
    protected MixinOptionsScreen(ITextComponent title) {
        super(title);
    }

    @Inject(at = @At("TAIL"), method = "init()V")
    private void open(CallbackInfo info) {
        this.addButton(new Button(this.width / 2 - 180, this.height / 6 + 72 - 6, 20, 20, new TranslationTextComponent("S"), (p_213059_1_) -> {
            this.minecraft.displayGuiScreen(new SodiumOptionsGUI(this));
        }));
    }
}
