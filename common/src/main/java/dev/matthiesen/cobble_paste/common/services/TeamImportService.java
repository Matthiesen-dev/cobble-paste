package dev.matthiesen.cobble_paste.common.services;

import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.util.PlayerExtensionsKt;
import dev.matthiesen.cobble_paste.common.converter.ShowdownToCobblemonConverter;
import dev.matthiesen.cobble_paste.common.formats.ShowdownTeam;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;

public final class TeamImportService {
    private TeamImportService() {
    }

    public static void importIntoParty(ServerPlayer player, ShowdownTeam team) {
        var party = PlayerExtensionsKt.party(player);
        var pc = PlayerExtensionsKt.pc(player);

        List<Pokemon> movedToPc = new ArrayList<>();
        for (int i = 0; i < party.size(); i++) {
            Pokemon current = party.get(i);
            if (current != null) {
                movedToPc.add(current);
            }
        }
        for (Pokemon pokemon : movedToPc) {
            party.remove(pokemon);
            pc.add(pokemon);
        }

        int inserted = 0;
        for (var entry : team.team()) {
            if (inserted >= 6) {
                break;
            }
            Pokemon converted = ShowdownToCobblemonConverter.convert(entry);
            if (converted == null) {
                continue;
            }
            party.add(converted);
            inserted++;
        }

        player.sendSystemMessage(Component.literal("Imported " + inserted + " Pokémon into your party.").withStyle(ChatFormatting.GREEN));
    }
}
