package dev.matthiesen.cobble_paste.common.formats;

import com.cobblemon.mod.common.api.pokemon.stats.Stats;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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

}
