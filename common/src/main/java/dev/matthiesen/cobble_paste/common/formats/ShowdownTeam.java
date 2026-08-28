package dev.matthiesen.cobble_paste.common.formats;

import java.util.List;

public record ShowdownTeam(
        String pokePasteId,
        List<ShowdownEntry> team
) {

}
