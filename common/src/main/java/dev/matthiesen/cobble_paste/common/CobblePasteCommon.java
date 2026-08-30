package dev.matthiesen.cobble_paste.common;

import dev.matthiesen.cobble_paste.common.commands.CobblePasteCommands;
import dev.matthiesen.cobble_paste.common.config.CobblePasteConfig;
import dev.matthiesen.cobble_paste.common.registry.PermissionsRegistry;
import dev.matthiesen.cobble_paste.common.services.PreviewService;
import dev.matthiesen.libs.faststats.Token;
import dev.matthiesen.matthiesen_core.common.AbstractCommonMod;
import dev.matthiesen.matthiesen_core.common.api.platform.loader.ModConfigType;
import org.jetbrains.annotations.NotNull;

public final class CobblePasteCommon extends AbstractCommonMod {
    public static final String MOD_ID = "cobble_paste";
    public static final String MOD_NAME = "Cobble Paste";
    public static @Token final String METRICS_TOKEN = "ff82c274e6c0ecf6d9eb2ab55c94b0e8";
    public static final CobblePasteCommon INSTANCE = new CobblePasteCommon();

    public CobblePasteCommon() {
       super(MOD_ID, MOD_NAME);
    }

    @Override
    public @Token @NotNull String getMetricsToken() {
       return METRICS_TOKEN;
    }

    public static String modConfig(String path) {
         return MOD_ID + "/" + path + ".toml";
    }

    public void initialize() {
       super.initialize();

       registerModConfig(MOD_ID, ModConfigType.STARTUP, CobblePasteConfig.PERMISSIONS_SPEC, modConfig("permissions"));
       registerModConfig(MOD_ID, ModConfigType.SERVER, CobblePasteConfig.SERVER_SPEC, modConfig("server"));

       PermissionsRegistry.init();
       PreviewService.init();
       getCommandsRegistryManager().registerCommand(CobblePasteCommands.CMD);

       createInfoLog("Initialized");
    }
}
