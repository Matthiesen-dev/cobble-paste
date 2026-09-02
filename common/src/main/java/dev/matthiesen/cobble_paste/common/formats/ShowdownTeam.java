package dev.matthiesen.cobble_paste.common.formats;

import java.util.ArrayList;
import java.util.List;

import static dev.matthiesen.cobble_paste.common.api.PokePasteParser.*;

public record ShowdownTeam(
        String pokePasteId,
        List<ShowdownEntry> team
) {
    public String serialize() {
        if (team == null || team.isEmpty()) {
            return "";
        }

        List<String> blocks = new ArrayList<>();
        for (ShowdownEntry entry : team){
            blocks.add(entry.serialize());
        }
        return String.join("\n\n", blocks);
    }

    public static ShowdownTeam fromPokePaste(String rawPasteText, String pokePasteId) {
        String resolvedPasteId = pokePasteId == null ? "" : pokePasteId;
        if (rawPasteText == null || rawPasteText.isBlank()) {
            return new ShowdownTeam(resolvedPasteId, List.of());
        }

        List<String> blocks = splitBlocks(rawPasteText);
        List<ShowdownEntry> entries = new ArrayList<>();
        for (String block : blocks) {
            ShowdownEntry entry = ShowdownEntry.fromPokePasteBlock(block);
            if (entry != null) {
                entries.add(entry);
            }
        }
        return new ShowdownTeam(resolvedPasteId, entries);
    }
}
