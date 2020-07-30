package me.jellysquid.mods.sodium.mixin.options;

import me.jellysquid.mods.sodium.client.SodiumClientMod;
import me.jellysquid.mods.sodium.client.gui.SodiumGameOptions;
import net.minecraft.client.GameSettings;
import net.minecraft.client.settings.CloudOption;
import net.minecraft.client.settings.GraphicsFanciness;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(GameSettings.class)
public class MixinGameOptions {
    @Shadow
    public int renderDistanceChunks;

    @Shadow
    public GraphicsFanciness field_238330_f_;

    /**
     * @author JellySquid
     * @reason Make the cloud render mode user-configurable
     */
    @Overwrite
    public CloudOption getCloudOption() {
        SodiumGameOptions options = SodiumClientMod.options();

        if (this.renderDistanceChunks < 4 || !options.quality.enableClouds) {
            return CloudOption.OFF;
        }

        return options.quality.cloudQuality.isFancy(this.field_238330_f_) ? CloudOption.FANCY : CloudOption.FAST;
    }
}
