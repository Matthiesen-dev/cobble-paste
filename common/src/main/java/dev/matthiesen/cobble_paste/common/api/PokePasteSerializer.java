package dev.matthiesen.cobble_paste.common.api;

import com.cobblemon.mod.common.api.pokemon.stats.Stats;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public final class PokePasteSerializer {
    private PokePasteSerializer() {
    }

    public static String renderStatLine(Map<Stats, Integer> stats) {
        List<String> parts = new ArrayList<>();
        for (Map.Entry<Stats, Integer> entry : stats.entrySet()) {
            parts.add(formatStatKey(entry.getKey()) + " " + entry.getValue());
        }
        return String.join(" / ", parts);
    }

    public static String formatStatKey(Stats stat) {
        if (stat == null) {
            return "";
        }
        return switch (stat) {
            case HP -> "HP";
            case ATTACK -> "Atk";
            case DEFENCE -> "Def";
            case SPECIAL_ATTACK -> "SpA";
            case SPECIAL_DEFENCE -> "SpD";
            case SPEED -> "Spe";
            default -> stat.name().toLowerCase(Locale.ROOT);
        };
    }
}
