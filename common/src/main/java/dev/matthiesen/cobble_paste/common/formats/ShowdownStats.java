package dev.matthiesen.cobble_paste.common.formats;

import com.cobblemon.mod.common.api.pokemon.stats.Stats;

import java.util.*;

import static dev.matthiesen.cobble_paste.common.api.PokePasteParser.parseIntMaybe;

public final class ShowdownStats {
    private ShowdownStats() {
    }

    public static Map<Stats, Integer> parseStatsPokePasteBlock(String raw) {
        Map<Stats, Integer> stats = new EnumMap<>(Stats.class);
        if (raw == null || raw.isBlank()) {
            return stats;
        }

        for (String part : raw.split("/")) {
            String[] tokens = part.trim().split("\\s+");
            Integer value = null;
            StringBuilder statName = new StringBuilder();
            for (String token : tokens) {
                Integer parsed = parseIntMaybe(token);
                if (parsed != null) {
                    value = parsed;
                } else {
                    statName.append(token);
                }
            }
            Stats stat = parseStatNameFromString(statName.toString());
            if (stat != null && value != null) {
                stats.put(stat, value);
            }
        }
        return stats;
    }

    public static String serializeStats(Map<Stats, Integer> stats) {
        List<String> parts = new ArrayList<>();
        for (Map.Entry<Stats, Integer> entry : stats.entrySet()) {
            parts.add(formatStatKey(entry.getKey()) + " " + entry.getValue());
        }
        return String.join(" / ", parts);
    }

    private static Stats parseStatNameFromString(String token) {
        String normalized = token.toLowerCase(Locale.ROOT);
        return switch (normalized) {
            case "hp" -> Stats.HP;
            case "atk", "attack" -> Stats.ATTACK;
            case "def", "defence", "defense" -> Stats.DEFENCE;
            case "spa", "specialattack", "special_attack" -> Stats.SPECIAL_ATTACK;
            case "spd", "specialdefence", "special_defence", "specialdefense", "special_defense" -> Stats.SPECIAL_DEFENCE;
            case "spe", "speed" -> Stats.SPEED;
            default -> null;
        };
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
