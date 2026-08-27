package dev.matthiesen.cobble_paste.common;

import dev.matthiesen.libs.faststats.Token;
import dev.matthiesen.matthiesen_core.common.AbstractCommonMod;
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

    public void initialize() {
        super.initialize();

       if (getCommonUtils().isModLoaded("cobblemon")) {
            createInfoLog("Cobblemon is loaded, Hello there Cobblemon!");
       }

        createInfoLog("Initialized");
    }
}
