package dev.matthiesen.cobble_paste.common.services;

import dev.matthiesen.cobble_paste.common.formats.ShowdownTeam;
import net.minecraft.server.level.ServerPlayer;

public interface PreviewAdapter {
    void open(ServerPlayer player, ShowdownTeam team);

    boolean isAvailable();
}
