package dev.matthiesen.cobble_paste.common.services.preview;

import dev.matthiesen.cobble_paste.common.formats.ShowdownTeam;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

final class UnavailablePreviewAdapter implements PreviewAdapter {
    @Override
    public void open(ServerPlayer player, ShowdownTeam team) {
        player.sendSystemMessage(Component.literal("Pokepaste previews require GooeyLibs to be installed on the server.")
                .withStyle(ChatFormatting.RED));
    }

    @Override
    public boolean isAvailable() {
        return false;
    }
}
