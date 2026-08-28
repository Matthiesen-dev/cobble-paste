package dev.matthiesen.cobble_paste.common.converter;

import com.cobblemon.mod.common.api.pokemon.stats.Stats;
import com.cobblemon.mod.common.pokemon.EVs;
import com.cobblemon.mod.common.pokemon.Gender;
import com.cobblemon.mod.common.pokemon.IVs;
import com.cobblemon.mod.common.pokemon.Pokemon;
import dev.matthiesen.cobble_paste.common.config.CobblePasteConfig;
import dev.matthiesen.cobble_paste.common.formats.ShowdownEntry;
import dev.matthiesen.cobble_paste.common.formats.ShowdownGender;
import dev.matthiesen.cobble_paste.common.mappings.SpeciesNameMapper;
import net.minecraft.core.registries.BuiltInRegistries;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class CobblemonToShowdownConverter {
    private CobblemonToShowdownConverter() {
    }

    public static ShowdownEntry convert(Pokemon pokemon) {
        if (pokemon == null) {
            return null;
        }

        String species = SpeciesNameMapper.toShowdown(pokemon.getSpecies().showdownId());
        String name = pokemon.getNickname() != null ? pokemon.getNickname().getString() : species;
        ShowdownGender gender = mapGender(pokemon.getGender());
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

    private static ShowdownGender mapGender(Gender gender) {
        if (gender == null) {
            return null;
        }
        return switch (gender) {
            case MALE -> ShowdownGender.MALE;
            case FEMALE -> ShowdownGender.FEMALE;
            case GENDERLESS -> ShowdownGender.GENDERLESS;
        };
    }
}
