package dev.matthiesen.cobble_paste.neoforge;

import dev.matthiesen.cobble_paste.common.CobblePasteCommon;
import net.neoforged.fml.common.Mod;

@Mod(CobblePasteCommon.MOD_ID)
public class CobblePasteNeoForge {
    public static final CobblePasteCommon INSTANCE = CobblePasteCommon.INSTANCE;

    public CobblePasteNeoForge() {
        INSTANCE.createInfoLog("Loading for NeoForge Mod Loader");
        INSTANCE.initialize();
    }
}
