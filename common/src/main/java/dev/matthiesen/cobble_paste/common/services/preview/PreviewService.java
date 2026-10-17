package dev.matthiesen.cobble_paste.common.services.preview;

import dev.matthiesen.cobble_paste.common.formats.ShowdownTeam;
import dev.matthiesen.cobble_paste.common.services.preview.core.CorePreviewAdapter;
import net.minecraft.server.level.ServerPlayer;

public final class PreviewService {
    private static PreviewAdapter adapter;

    private PreviewService() {
    }

    public static void init() {
        adapter = new CorePreviewAdapter();
    }

    public static boolean isAvailable() {
        return adapter != null && adapter.isAvailable();
    }

    public static void open(ServerPlayer player, ShowdownTeam team) {
        adapter.open(player, team);
    }
}
