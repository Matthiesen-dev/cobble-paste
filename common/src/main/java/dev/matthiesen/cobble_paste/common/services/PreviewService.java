package dev.matthiesen.cobble_paste.common.services;

import dev.matthiesen.cobble_paste.common.CobblePasteCommon;
import dev.matthiesen.cobble_paste.common.formats.ShowdownTeam;
import dev.matthiesen.cobble_paste.common.services.gooey.GooeyPreviewAdapter;
import net.minecraft.server.level.ServerPlayer;

public final class PreviewService {
    private static final String GOOEYLIBS_MOD_ID = "gooeylibs";
    private static PreviewAdapter adapter;

    private PreviewService() {
    }

    public static void init() {
        if (CobblePasteCommon.INSTANCE.getCommonUtils().isModLoaded(GOOEYLIBS_MOD_ID)) {
            adapter = new GooeyPreviewAdapter();
            CobblePasteCommon.INSTANCE.createInfoLog("GooeyLibs detected; Pokepast previews are enabled");
        } else {
            adapter = new UnavailablePreviewAdapter();
            CobblePasteCommon.INSTANCE.createInfoLog("GooeyLibs not detected; Pokepast previews are disabled");
        }
    }

    public static boolean isAvailable() {
        return adapter != null && adapter.isAvailable();
    }

    public static void open(ServerPlayer player, ShowdownTeam team) {
        adapter.open(player, team);
    }
}
