package me.jellysquid.mods.sodium.client;

import me.jellysquid.mods.sodium.client.gui.SodiumGameOptions;
import me.jellysquid.mods.sodium.client.gui.SodiumOptionsGUI;
import me.jellysquid.mods.sodium.client.util.UnsafeUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.IngameMenuScreen;
import net.minecraft.client.gui.screen.OptionsScreen;
import net.minecraft.client.gui.screen.VideoSettingsScreen;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

@Mod("sodium")
@Mod.EventBusSubscriber(modid = "sodium")
public class SodiumClientMod {
    private static SodiumGameOptions CONFIG;
    private static Logger LOGGER;

    public static SodiumGameOptions options() {
        if (CONFIG == null) {
            CONFIG = loadConfig();
        }

        return CONFIG;
    }

    public static Logger logger() {
        if (LOGGER == null) {
            LOGGER = LogManager.getLogger("Sodium");
        }

        return LOGGER;
    }

    private static SodiumGameOptions loadConfig() {
        SodiumGameOptions config = SodiumGameOptions.load(new File("config/sodium-options.json"));
        onConfigChanged(config);

        return config;
    }

    public static void onConfigChanged(SodiumGameOptions options) {
        UnsafeUtil.setEnabled(options.advanced.useMemoryIntrinsics);
    }
}
