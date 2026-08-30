package dev.matthiesen.cobble_paste.common.extensions;

import com.cobblemon.mod.common.pokemon.Pokemon;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class PartyCache {
    private final Map<String, List<Pokemon>> cache = new ConcurrentHashMap<>();

    public PartyCache() {
    }

    public void put(String key, List<Pokemon> party) {
        cache.put(key, party);
    }

    public List<Pokemon> get(String key) {
        return cache.get(key);
    }

    public void invalidateEntry(String key) {
        cache.remove(key);
    }

    public void clearCache() {
        cache.clear();
    }
}
