package dev.matthiesen.cobble_paste.fabric;

import dev.matthiesen.cobble_paste.common.CobblePasteCommon;
import net.fabricmc.api.ModInitializer;

public class CobblePasteFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        var instance = CobblePasteCommon.INSTANCE;
        instance.createInfoLog("Loading for Fabric Mod Loader");
        instance.initialize();
    }
}
