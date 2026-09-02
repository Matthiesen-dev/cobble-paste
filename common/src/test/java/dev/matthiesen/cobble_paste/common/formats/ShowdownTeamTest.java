package dev.matthiesen.cobble_paste.common.formats;

import com.cobblemon.mod.common.api.pokemon.stats.Stats;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ShowdownTeamTest {
    @Test
    void parsesStandardShowdownPaste() {
        String paste = """
                Solosis @ Eviolite
                Ability: Regenerator
                Level: 5
                EVs: 76 HP / 120 Def / 240 SpA / 40 SpD
                Quiet Nature
                - Trick Room
                - Psychic

                Tiny Mime (Mime Jr.) (F)
                Ability: Soundproof
                Level: 5
                IVs: 0 Atk / 31 Spe
                """;

        ShowdownTeam team = ShowdownTeam.fromPokePaste(paste, "0123456789abcdef");

        assertEquals("0123456789abcdef", team.pokePasteId());
        assertEquals(2, team.team().size());

        ShowdownEntry solosis = team.team().getFirst();
        assertEquals("Solosis", solosis.species());
        assertEquals("Eviolite", solosis.heldItem().orElseThrow());
        assertEquals("Regenerator", solosis.ability().orElseThrow());
        assertEquals("Quiet", solosis.nature().orElseThrow());
        assertEquals(Integer.valueOf(5), solosis.level().orElseThrow());
        assertEquals(Integer.valueOf(76), solosis.evs().get(Stats.HP));
        assertEquals(Integer.valueOf(120), solosis.evs().get(Stats.DEFENCE));
        assertEquals(Integer.valueOf(240), solosis.evs().get(Stats.SPECIAL_ATTACK));
        assertEquals(Integer.valueOf(40), solosis.evs().get(Stats.SPECIAL_DEFENCE));
        assertEquals(2, solosis.moves().size());

        ShowdownEntry mime = team.team().get(1);
        assertEquals("Tiny Mime", mime.name());
        assertEquals("Mime Jr.", mime.species());
        assertEquals(ShowdownGender.FEMALE, mime.gender().orElseThrow());
        assertEquals(Integer.valueOf(0), mime.ivs().get(Stats.ATTACK));
        assertEquals(Integer.valueOf(31), mime.ivs().get(Stats.SPEED));
    }
}
