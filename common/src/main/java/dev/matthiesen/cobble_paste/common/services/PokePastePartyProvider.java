package dev.matthiesen.cobble_paste.common.services;

import com.cobblemon.mod.common.api.npc.NPCPartyProvider;
import com.cobblemon.mod.common.api.storage.party.NPCPartyStore;
import com.cobblemon.mod.common.entity.npc.NPCEntity;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.matthiesen.cobble_paste.common.api.PokePasteApiClient;
import dev.matthiesen.cobble_paste.common.formats.ShowdownTeam;
import dev.matthiesen.cobble_paste.common.util.PartyCache;
import kotlin.jvm.functions.Function1;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class PokePastePartyProvider implements NPCPartyProvider {
    public static final String TYPE = "cobble_paste:poke_paste";
    public static Function1<String, NPCPartyProvider> PROVIDER = (id) -> new PokePastePartyProvider();

    private static final PartyCache CACHE = new PartyCache();

    public static void register() {
        NPCPartyProvider.Companion.getTypes().put(TYPE, PROVIDER);
    }

    private boolean isStatic = true;
    private String pokePasteId = "";


    @Override
    public @NotNull String getType() {
        return TYPE;
    }

    @Override
    public boolean isStatic() {
        return isStatic;
    }

    @Override
    public void loadFromJSON(@NotNull JsonElement jsonElement) {
        JsonObject object = jsonElement.getAsJsonObject();

        if (object.has("isStatic")) {
            isStatic = object.get("isStatic").getAsBoolean();
        }
        if (object.has("pokePasteId")) {
            pokePasteId = object.get("pokePasteId").getAsString();
        }
    }

    @Override
    public @NotNull NPCPartyStore provide(@NotNull NPCEntity npcEntity, int i, @NotNull List<? extends ServerPlayer> list) {
        List<Pokemon> party = this.loadTeam(i);
        if (party.isEmpty()) {
            NPCPartyStore empty = new NPCPartyStore(npcEntity);
            empty.initialize();
            return empty;
        }

        NPCPartyStore partyStore = new NPCPartyStore(npcEntity);
        int count = 0;

        for (Pokemon pokemon : party) {
            if (count >= 6) {
                break;
            }

            partyStore.add(pokemon);
            ++count;
        }

        partyStore.initialize();
        return partyStore;
    }

    private List<Pokemon> loadTeam(int level) {
        String cacheKey = pokePasteId + "_" + level;

        if (this.isStatic) {
            List<Pokemon> cachedParty = CACHE.get(cacheKey);
            if (cachedParty != null) {
                return cachedParty;
            }
        }

        String rawPaste = PokePasteApiClient.fetchRawPaste(pokePasteId).join();
        var showdownTeam = ShowdownTeam.fromPokePaste(rawPaste, PokePasteApiClient.extractPasteId(pokePasteId));
        List<Pokemon> party = new ArrayList<>();

        for (var entry : showdownTeam.team()) {
            if (party.size() >= 6) {
                break;
            }

            Pokemon pokemon = entry.toPokemon();
            if (entry.level().isEmpty()) {
                pokemon.setLevel(level);
            }
            party.add(pokemon);
        }

        List<Pokemon> loadedParty = List.copyOf(party);
        if (this.isStatic) {
            CACHE.put(cacheKey, loadedParty);
        }
        return loadedParty;
    }
}
