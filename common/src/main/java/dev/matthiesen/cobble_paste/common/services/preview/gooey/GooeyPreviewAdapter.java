package dev.matthiesen.cobble_paste.common.services.preview.gooey;

import dev.matthiesen.cobble_paste.common.formats.ShowdownTeam;
import dev.matthiesen.cobble_paste.common.services.preview.PreviewAdapter;
import net.minecraft.server.level.ServerPlayer;

public final class GooeyPreviewAdapter implements PreviewAdapter {
    @Override
    public void open(ServerPlayer player, ShowdownTeam team) {
        new PokepastesPreviewGui(player, team).open();
    }

    @Override
    public boolean isAvailable() {
        return true;
    }
}
