package dev.matthiesen.cobble_paste.common.parser;

import com.cobblemon.mod.common.api.pokemon.stats.Stats;
import dev.matthiesen.cobble_paste.common.formats.ShowdownEntry;
import dev.matthiesen.cobble_paste.common.formats.ShowdownGender;
import dev.matthiesen.cobble_paste.common.formats.ShowdownTeam;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public final class PokePasteParser {
    private PokePasteParser() {
    }

    public static ShowdownTeam parse(String rawPasteText, String pokePasteId) {
        String resolvedPasteId = pokePasteId == null ? "" : pokePasteId;
        if (rawPasteText == null || rawPasteText.isBlank()) {
            return new ShowdownTeam(resolvedPasteId, List.of());
        }

        List<String> blocks = splitBlocks(rawPasteText);
        List<ShowdownEntry> entries = new ArrayList<>();
        for (String block : blocks) {
            ShowdownEntry entry = parseBlock(block);
            if (entry != null) {
                entries.add(entry);
            }
        }
        return new ShowdownTeam(resolvedPasteId, entries);
    }

    private static ShowdownEntry parseBlock(String block) {
        String[] lines = block.replace("\r", "").split("\\n");
        List<String> bodyLines = new ArrayList<>();
        Map<String, String> fields = new LinkedHashMap<>();
        String header = "";
        for (String line : lines) {
            String trimmed = line.trim();
            if (trimmed.isEmpty()) {
                continue;
            }
            if (trimmed.startsWith("||")) {
                bodyLines.add(trimmed);
            } else if (header.isEmpty()) {
                header = trimmed;
            } else if (trimmed.startsWith("- ")) {
                bodyLines.add(trimmed);
            } else if (trimmed.endsWith(" Nature")) {
                fields.put("nature", trimmed.substring(0, trimmed.length() - " Nature".length()).trim());
            } else {
                int separator = trimmed.indexOf(':');
                if (separator > 0) {
                    fields.put(
                            trimmed.substring(0, separator).trim().toLowerCase(Locale.ROOT),
                            trimmed.substring(separator + 1).trim()
                    );
                }
            }
        }

        for (String line : bodyLines) {
            if (!line.startsWith("||")) {
                continue;
            }
            String[] pair = line.substring(2).split("\\|", 2);
            if (pair.length < 2) {
                continue;
            }
            fields.put(pair[0].trim().toLowerCase(Locale.ROOT), pair[1].trim());
        }

        String species = firstNonBlank(
                fields.get("species"),
                extractSpeciesFromHeader(header),
                ""
        );
        if (species.isBlank()) {
            return null;
        }

        String nickname = firstNonBlank(fields.get("name"), extractNicknameFromHeader(header), species);
        String genderText = firstNonBlank(fields.get("gender"), extractGenderFromHeader(header));
        ShowdownGender gender = getGender(genderText);

        String item = firstNonBlank(
                fields.get("item"),
                fields.get("held item"),
                fields.get("helditem"),
                extractItemFromHeader(header)
        );
        String ability = firstNonBlank(fields.get("ability"));
        String nature = firstNonBlank(fields.get("nature"));
        Optional<Integer> level = parseOptionalInt(fields.get("level"));
        Optional<Boolean> shiny = parseOptionalBoolean(fields.get("shiny"));
        Optional<String> teraType = Optional.of(firstNonBlank(fields.get("tera type"), fields.get("teratype"), fields.get("tera")));
        Optional<Integer> happiness = parseOptionalInt(fields.get("happiness"));

        Map<Stats, Integer> evs = parseStatsBlock(fields.get("evs"));
        Map<Stats, Integer> ivs = parseStatsBlock(fields.get("ivs"));
        List<String> moves = new ArrayList<>();
        for (String line : lines) {
            String trimmed = line.trim();
            if (trimmed.startsWith("- ")) {
                String move = trimmed.substring(2).trim();
                if (!move.isBlank()) {
                    moves.add(move);
                }
            }
        }

        return new ShowdownEntry(
                nickname,
                species,
                Optional.ofNullable(gender),
                Optional.ofNullable(item.isBlank() ? null : item),
                Optional.ofNullable(ability.isBlank() ? null : ability),
                Optional.ofNullable(nature.isBlank() ? null : nature),
                level,
                shiny,
                teraType.filter(value -> !value.isBlank()),
                evs,
                ivs,
                moves,
                happiness
        );
    }

    private static @Nullable ShowdownGender getGender(String genderText) {
        ShowdownGender gender = null;
        if (genderText != null && !genderText.isBlank()) {
            String normalized = genderText.trim();
            if (normalized.equalsIgnoreCase("m") || normalized.equalsIgnoreCase("male")) {
                gender = ShowdownGender.MALE;
            } else if (normalized.equalsIgnoreCase("f") || normalized.equalsIgnoreCase("female")) {
                gender = ShowdownGender.FEMALE;
            } else if (normalized.equalsIgnoreCase("n") || normalized.equalsIgnoreCase("genderless")) {
                gender = ShowdownGender.GENDERLESS;
            }
        }
        return gender;
    }

    private static Map<Stats, Integer> parseStatsBlock(String raw) {
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

    private static Optional<Integer> parseOptionalInt(String value) {
        if (value == null || value.isBlank()) {
            return Optional.empty();
        }
        Integer parsed = parseIntMaybe(value);
        return parsed == null ? Optional.empty() : Optional.of(parsed);
    }

    private static Optional<Boolean> parseOptionalBoolean(String value) {
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

    private static String firstNonBlank(String... values) {
        for (String value : values) {
            if (value != null && !value.isBlank()) {
                return value;
            }
        }
        return "";
    }

    private static String extractSpeciesFromHeader(String header) {
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

    private static String extractNicknameFromHeader(String header) {
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

    private static String extractGenderFromHeader(String header) {
        String cleaned = stripItem(header);
        if (cleaned.endsWith(" (M)") || cleaned.endsWith(" (F)") || cleaned.endsWith(" (N)")) {
            return cleaned.substring(cleaned.length() - 2, cleaned.length() - 1);
        }
        return "";
    }

    private static String extractItemFromHeader(String header) {
        if (header == null) {
            return "";
        }
        int separator = header.indexOf(" @ ");
        return separator < 0 ? "" : header.substring(separator + 3).trim();
    }

    private static String stripItemAndGender(String header) {
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

    private static List<String> splitBlocks(String rawPasteText) {
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
