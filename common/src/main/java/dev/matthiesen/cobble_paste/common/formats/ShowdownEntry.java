package dev.matthiesen.cobble_paste.common.formats;

import com.cobblemon.mod.common.api.pokemon.PokemonProperties;
import com.cobblemon.mod.common.api.pokemon.stats.Stats;
import com.cobblemon.mod.common.pokemon.EVs;
import com.cobblemon.mod.common.pokemon.IVs;
import com.cobblemon.mod.common.pokemon.Pokemon;
import dev.matthiesen.cobble_paste.common.config.CobblePasteConfig;
import dev.matthiesen.cobble_paste.common.mappings.SpeciesNameMapper;
import net.minecraft.core.registries.BuiltInRegistries;

import java.util.*;

import static dev.matthiesen.cobble_paste.common.api.PokePasteParser.*;
import static dev.matthiesen.cobble_paste.common.api.PokePasteSerializer.*;

public record ShowdownEntry(
        String name,
        String species,
        Optional<ShowdownGender> gender,
        Optional<String> heldItem,
        Optional<String> ability,
        Optional<String> nature,
        Optional<Integer> level,
        Optional<Boolean> shiny,
        Optional<String> teraType,
        Map<Stats, Integer> evs,
        Map<Stats, Integer> ivs,
        List<String> moves,
        Optional<Integer> happiness
) {

    public Pokemon toPokemon() {
        StringBuilder propertyBuilder = new StringBuilder();
        propertyBuilder.append(SpeciesNameMapper.toCobblemon(species));

        level.ifPresent(integer -> propertyBuilder.append(" level=").append(integer));
        gender.ifPresent(g -> propertyBuilder.append(" gender=").append(g.name().toLowerCase(Locale.ROOT)));
        nature.ifPresent(n -> {
            if (!n.isBlank()) {
                propertyBuilder.append(" nature=").append(n);
            }
        });
        ability.ifPresent(a -> {
            if (!a.isBlank()) {
                propertyBuilder.append(" ability=").append(a);
            }
        });
        shiny.ifPresent(aBoolean -> propertyBuilder.append(" shiny=").append(aBoolean));
        heldItem.ifPresent(item -> {
            if (!item.isBlank()) {
                String mapped = CobblePasteConfig.getItemId(item);
                if (mapped != null && !mapped.startsWith("UNMAPPED:")) {
                    propertyBuilder.append(" helditem=").append(mapped);
                }
            }
        });

        if (moves != null && !moves.isEmpty()) {
            propertyBuilder.append(" moves=").append(String.join(",", moves));
        }

        for (var stat : Stats.values()) {
            if (ivs != null && ivs.containsKey(stat)) {
                propertyBuilder.append(" ").append(getPropertyKey(stat)).append("_iv=").append(ivs.get(stat));
            }
            if (evs != null && evs.containsKey(stat)) {
                propertyBuilder.append(" ").append(getPropertyKey(stat)).append("_ev=").append(evs.get(stat));
            }
        }

        return PokemonProperties.Companion.parse(propertyBuilder.toString()).create(null);
    }
    
    public String serialize() {

        StringBuilder builder = new StringBuilder();
        String primaryName = name;
        if (primaryName == null || primaryName.isBlank()) {
            primaryName = species;
        }

        if (heldItem.isPresent() && !heldItem.get().isBlank()) {
            builder.append(primaryName).append(" @ ").append(heldItem.get()).append('\n');
        } else {
            builder.append(primaryName).append('\n');
        }

        if (level.isPresent()) {
            builder.append("Level: ").append(level).append('\n');
        }
        if (gender.isPresent()) {
            String genderString = gender.get().getShowdownString();
            builder.append("Gender: ").append(genderString).append('\n');
        }
        if (nature.isPresent() && !nature.get().isBlank()) {
            builder.append("Nature: ").append(nature.get()).append('\n');
        }
        if (ability.isPresent() && !ability.get().isBlank()) {
            builder.append("Ability: ").append(ability.get()).append('\n');
        }
        if (evs != null && !evs.isEmpty()) {
            builder.append("EVs: ").append(renderStatLine(evs)).append('\n');
        }
        if (ivs != null && !ivs.isEmpty()) {
            builder.append("IVs: ").append(renderStatLine(ivs)).append('\n');
        }
        shiny.ifPresent(aBoolean -> builder.append("Shiny: ").append(aBoolean ? "Yes" : "No").append('\n'));
        happiness.ifPresent(integer -> builder.append("Happiness: ").append(integer).append('\n'));
        if (teraType.isPresent() && !teraType.get().isBlank()) {
            builder.append("Tera Type: ").append(teraType.get()).append('\n');
        }
        if (moves != null && !moves.isEmpty()) {
            for (String move : moves) {
                builder.append("- ").append(move).append('\n');
            }
        }
        return builder.toString().trim();
    }

    public static ShowdownEntry fromPokemon(Pokemon pokemon) {
        if (pokemon == null) {
            return null;
        }

        String species = SpeciesNameMapper.toShowdown(pokemon.getSpecies().showdownId());
        String name = pokemon.getNickname() != null ? pokemon.getNickname().getString() : species;
        ShowdownGender gender = ShowdownGender.fromGender(pokemon.getGender());
        Optional<String> item = Optional.empty();
        if (!pokemon.heldItem().isEmpty()) {
            String registryId = BuiltInRegistries.ITEM.getKey(pokemon.heldItem().getItem()).toString();
            String showdownName = CobblePasteConfig.getShowdownName(registryId);
            item = Optional.of(showdownName != null ? showdownName : registryId);
        }

        Optional<String> ability = Optional.of(pokemon.getAbility().getName());
        Optional<String> nature = Optional.of(pokemon.getEffectiveNature().getName().toString());
        Optional<Integer> level = Optional.of(pokemon.getLevel());
        Optional<Boolean> shiny = Optional.of(pokemon.getShiny());
        Optional<String> teraType = Optional.of(pokemon.getTeraType().getName());

        EVs evStats = pokemon.getEvs();
        Map<Stats, Integer> evs = new EnumMap<>(Stats.class);
        for (Stats stat : Stats.values()) {
            Integer value = evStats.get(stat);
            if (value != null && value > 0) {
                evs.put(stat, value);
            }
        }

        IVs ivStats = pokemon.getIvs();
        Map<Stats, Integer> ivs = new EnumMap<>(Stats.class);
        for (Stats stat : Stats.values()) {
            Integer value = ivStats.get(stat);
            if (value != null && value > 0) {
                ivs.put(stat, value);
            }
        }

        List<String> moves = new java.util.ArrayList<>();
        for (var move : pokemon.getMoveSet().getMoves()) {
            if (move != null) {
                moves.add(move.getTemplate().getName());
            }
        }

        return new ShowdownEntry(
                name,
                species,
                Optional.ofNullable(gender),
                item,
                ability,
                nature,
                level,
                shiny,
                teraType,
                evs,
                ivs,
                moves,
                Optional.empty()
        );
    }

    public static ShowdownEntry fromPokePasteBlock(String block) {
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
        ShowdownGender gender = ShowdownGender.fromString(genderText);

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
