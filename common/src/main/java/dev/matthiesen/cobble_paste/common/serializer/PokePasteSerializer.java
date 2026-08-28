package dev.matthiesen.cobble_paste.common.serializer;

import com.cobblemon.mod.common.api.pokemon.stats.Stats;
import dev.matthiesen.cobble_paste.common.formats.ShowdownEntry;
import dev.matthiesen.cobble_paste.common.formats.ShowdownGender;
import dev.matthiesen.cobble_paste.common.formats.ShowdownTeam;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public final class PokePasteSerializer {
    private PokePasteSerializer() {
    }

    public static String serialize(ShowdownTeam team) {
        if (team == null || team.team() == null || team.team().isEmpty()) {
            return "";
        }

        List<String> blocks = new ArrayList<>();
        for (ShowdownEntry entry : team.team()) {
            blocks.add(serializeEntry(entry));
        }
        return String.join("\n\n", blocks);
    }

    public static String serializeEntry(ShowdownEntry entry) {
        if (entry == null) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        String primaryName = entry.name();
        if (primaryName == null || primaryName.isBlank()) {
            primaryName = entry.species();
        }

        if (entry.heldItem().isPresent() && !entry.heldItem().get().isBlank()) {
            builder.append(primaryName).append(" @ ").append(entry.heldItem().get()).append('\n');
        } else {
            builder.append(primaryName).append('\n');
        }

        if (entry.level().isPresent()) {
            builder.append("Level: ").append(entry.level().get()).append('\n');
        }
        if (entry.gender().isPresent()) {
            String gender = toShowdownGender(entry.gender().get());
            builder.append("Gender: ").append(gender).append('\n');
        }
        if (entry.nature().isPresent() && !entry.nature().get().isBlank()) {
            builder.append("Nature: ").append(entry.nature().get()).append('\n');
        }
        if (entry.ability().isPresent() && !entry.ability().get().isBlank()) {
            builder.append("Ability: ").append(entry.ability().get()).append('\n');
        }
        if (entry.evs() != null && !entry.evs().isEmpty()) {
            builder.append("EVs: ").append(renderStatLine(entry.evs())).append('\n');
        }
        if (entry.ivs() != null && !entry.ivs().isEmpty()) {
            builder.append("IVs: ").append(renderStatLine(entry.ivs())).append('\n');
        }
        if (entry.shiny().isPresent()) {
            builder.append("Shiny: ").append(entry.shiny().get() ? "Yes" : "No").append('\n');
        }
        if (entry.teraType().isPresent() && !entry.teraType().get().isBlank()) {
            builder.append("Tera Type: ").append(entry.teraType().get()).append('\n');
        }
        if (entry.happiness().isPresent()) {
            builder.append("Happiness: ").append(entry.happiness().get()).append('\n');
        }
        if (entry.moves() != null && !entry.moves().isEmpty()) {
            for (String move : entry.moves()) {
                builder.append("- ").append(move).append('\n');
            }
        }
        return builder.toString().trim();
    }

    private static String renderStatLine(Map<Stats, Integer> stats) {
        List<String> parts = new ArrayList<>();
        for (Map.Entry<Stats, Integer> entry : stats.entrySet()) {
            parts.add(formatStatKey(entry.getKey()) + " " + entry.getValue());
        }
        return String.join(" / ", parts);
    }

    private static String formatStatKey(Stats stat) {
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

    private static String toShowdownGender(ShowdownGender gender) {
        if (gender == null) {
            return "";
        }
        return switch (gender) {
            case MALE -> "M";
            case FEMALE -> "F";
            case GENDERLESS -> "N";
        };
    }
}
