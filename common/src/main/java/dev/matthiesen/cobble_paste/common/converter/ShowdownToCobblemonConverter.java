package dev.matthiesen.cobble_paste.common.converter;

import com.cobblemon.mod.common.api.pokemon.PokemonProperties;
import com.cobblemon.mod.common.api.pokemon.stats.Stats;
import com.cobblemon.mod.common.pokemon.Pokemon;
import dev.matthiesen.cobble_paste.common.config.CobblePasteConfig;
import dev.matthiesen.cobble_paste.common.formats.ShowdownEntry;
import dev.matthiesen.cobble_paste.common.mappings.SpeciesNameMapper;

import java.util.Locale;

public final class ShowdownToCobblemonConverter {
    private ShowdownToCobblemonConverter() {
    }

    public static Pokemon convert(ShowdownEntry entry) {
        if (entry == null) {
            return null;
        }

        StringBuilder propertyBuilder = new StringBuilder();
        propertyBuilder.append(SpeciesNameMapper.toCobblemon(entry.species()));

        if (entry.level().isPresent()) {
            propertyBuilder.append(" level=").append(entry.level().get());
        }
        if (entry.gender().isPresent()) {
            propertyBuilder.append(" gender=").append(entry.gender().get().name().toLowerCase(Locale.ROOT));
        }
        if (entry.nature().isPresent() && !entry.nature().get().isBlank()) {
            propertyBuilder.append(" nature=").append(entry.nature().get());
        }
        if (entry.ability().isPresent() && !entry.ability().get().isBlank()) {
            propertyBuilder.append(" ability=").append(entry.ability().get());
        }
        if (entry.shiny().isPresent()) {
            propertyBuilder.append(" shiny=").append(entry.shiny().get());
        }
        if (entry.heldItem().isPresent() && !entry.heldItem().get().isBlank()) {
            String mapped = CobblePasteConfig.getItemId(entry.heldItem().get());
            if (mapped != null && !mapped.startsWith("UNMAPPED:")) {
                propertyBuilder.append(" helditem=").append(mapped);
            }
        }
        if (entry.moves() != null && !entry.moves().isEmpty()) {
            propertyBuilder.append(" moves=").append(String.join(",", entry.moves()));
        }

        for (var stat : Stats.values()) {
            if (entry.ivs() != null && entry.ivs().containsKey(stat)) {
                propertyBuilder.append(" ").append(getPropertyKey(stat)).append("_iv=").append(entry.ivs().get(stat));
            }
            if (entry.evs() != null && entry.evs().containsKey(stat)) {
                propertyBuilder.append(" ").append(getPropertyKey(stat)).append("_ev=").append(entry.evs().get(stat));
            }
        }

        return PokemonProperties.Companion.parse(propertyBuilder.toString()).create(null);
    }

    private static String getPropertyKey(Stats stat) {
        if (stat == null) {
            return "";
        }
        return switch (stat) {
            case HP -> "hp";
            case ATTACK -> "atk";
            case DEFENCE -> "def";
            case SPECIAL_ATTACK -> "spa";
            case SPECIAL_DEFENCE -> "spd";
            case SPEED -> "spe";
            default -> stat.name().toLowerCase(Locale.ROOT);
        };
    }
}
