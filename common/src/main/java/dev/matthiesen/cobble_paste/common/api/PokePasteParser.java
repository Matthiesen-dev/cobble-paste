package dev.matthiesen.cobble_paste.common.api;

import com.cobblemon.mod.common.api.pokemon.stats.Stats;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public final class PokePasteParser {
    private PokePasteParser() {
    }

    public static Map<Stats, Integer> parseStatsBlock(String raw) {
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
            Stats stat = parseStatName(statName.toString());
            if (stat != null && value != null) {
                stats.put(stat, value);
            }
        }
        return stats;
    }

    private static Stats parseStatName(String token) {
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

    public static Optional<Integer> parseOptionalInt(String value) {
        if (value == null || value.isBlank()) {
            return Optional.empty();
        }
        Integer parsed = parseIntMaybe(value);
        return parsed == null ? Optional.empty() : Optional.of(parsed);
    }

    public static Optional<Boolean> parseOptionalBoolean(String value) {
        if (value == null || value.isBlank()) {
            return Optional.empty();
        }
        String normalized = value.trim();
        if (normalized.equalsIgnoreCase("yes") || normalized.equalsIgnoreCase("y") || normalized.equalsIgnoreCase("true")) {
            return Optional.of(true);
        }
        if (normalized.equalsIgnoreCase("no") || normalized.equalsIgnoreCase("n") || normalized.equalsIgnoreCase("false")) {
            return Optional.of(false);
        }
        return Optional.empty();
    }

    private static Integer parseIntMaybe(String value) {
        try {
            return Integer.parseInt(value.replaceAll("[^0-9-]", ""));
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    public static String firstNonBlank(String... values) {
        for (String value : values) {
            if (value != null && !value.isBlank()) {
                return value;
            }
        }
        return "";
    }

    public static String extractSpeciesFromHeader(String header) {
        if (header == null || header.isBlank()) {
            return "";
        }

        String cleaned = stripItemAndGender(header);
        int speciesStart = cleaned.indexOf(" (");
        if (speciesStart >= 0 && cleaned.endsWith(")")) {
            return cleaned.substring(speciesStart + 2, cleaned.length() - 1).trim();
        }
        return cleaned.trim();
    }

    public static String extractNicknameFromHeader(String header) {
        if (header == null || header.isBlank()) {
            return "";
        }

        String cleaned = stripItemAndGender(header);
        if (cleaned.endsWith(")") && cleaned.contains(" (")) {
            String nickname = cleaned.substring(0, cleaned.indexOf(" ("));
            if (!nickname.isBlank()) {
                return nickname.trim();
            }
        }
        return "";
    }

    public static String extractGenderFromHeader(String header) {
        String cleaned = stripItem(header);
        if (cleaned.endsWith(" (M)") || cleaned.endsWith(" (F)") || cleaned.endsWith(" (N)")) {
            return cleaned.substring(cleaned.length() - 2, cleaned.length() - 1);
        }
        return "";
    }

    public static String extractItemFromHeader(String header) {
        if (header == null) {
            return "";
        }
        int separator = header.indexOf(" @ ");
        return separator < 0 ? "" : header.substring(separator + 3).trim();
    }

    public static String stripItemAndGender(String header) {
        String cleaned = stripItem(header);
        if (cleaned.endsWith(" (M)") || cleaned.endsWith(" (F)") || cleaned.endsWith(" (N)")) {
            cleaned = cleaned.substring(0, cleaned.length() - 4);
        }
        return cleaned;
    }

    private static String stripItem(String header) {
        String cleaned = header;
        int itemSeparator = cleaned.indexOf(" @ ");
        if (itemSeparator >= 0) {
            cleaned = cleaned.substring(0, itemSeparator);
        }
        return cleaned.trim();
    }

    public static List<String> splitBlocks(String rawPasteText) {
        List<String> blocks = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        for (String line : rawPasteText.replace("\r", "").split("\\n")) {
            if (line.trim().isEmpty()) {
                if (!current.isEmpty()) {
                    blocks.add(current.toString().trim());
                    current.setLength(0);
                }
                continue;
            }
            current.append(line).append('\n');
        }
        if (!current.isEmpty()) {
            blocks.add(current.toString().trim());
        }
        return blocks;
    }
}
