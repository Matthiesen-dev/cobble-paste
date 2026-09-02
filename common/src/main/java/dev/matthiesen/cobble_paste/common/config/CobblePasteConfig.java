package dev.matthiesen.cobble_paste.common.config;

import dev.matthiesen.cobble_paste.common.util.HeldItemMapper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.LinkedHashMap;
import java.util.Map;

public final class CobblePasteConfig {
    public static final ServerConfig SERVER_CONFIG;
    public static final ModConfigSpec SERVER_SPEC;

    public static final PermissionsConfig PERMISSIONS_CONFIG;
    public static final ModConfigSpec PERMISSIONS_SPEC;

    static {
        Pair<ServerConfig, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(ServerConfig::new);
        SERVER_CONFIG = specPair.getLeft();
        SERVER_SPEC = specPair.getRight();

        Pair<PermissionsConfig, ModConfigSpec> permissionsSpecPair = new ModConfigSpec.Builder().configure(PermissionsConfig::new);
        PERMISSIONS_CONFIG = permissionsSpecPair.getLeft();
        PERMISSIONS_SPEC = permissionsSpecPair.getRight();
    }

    private CobblePasteConfig() {
    }

    public static String getPasteAuthor() {
        return SERVER_CONFIG.pasteAuthor.get();
    }

    public static String getItemId(String showdownItemName) {
        Item item = HeldItemMapper.getItemForShowdownId(showdownItemName);
        if (item != null) {
            return BuiltInRegistries.ITEM.getKey(item).toString();
        }
        return getConfiguredItemId(showdownItemName);
    }

    public static String getConfiguredItemId(String showdownItemName) {
        if (showdownItemName == null || showdownItemName.isBlank()) {
            return null;
        }

        String normalized = showdownItemName.trim();
        for (Map.Entry<String, String> entry : itemMappings().entrySet()) {
            if (entry.getKey().equalsIgnoreCase(normalized)) {
                return entry.getValue();
            }
        }

        String lookup = normalized
                .replace("’", "'")
                .replace("-", " ")
                .trim();
        for (Map.Entry<String, String> entry : itemMappings().entrySet()) {
            if (entry.getKey().equalsIgnoreCase(lookup)) {
                return entry.getValue();
            }
        }

        return null;
    }

    public static Map<String, String> itemMappings() {
        Map<String, String> mappings = new LinkedHashMap<>();
        for (String entry : SERVER_CONFIG.itemMappings.get()) {
            int splitIndex = entry.indexOf('=');
            if (splitIndex <= 0 || splitIndex == entry.length() - 1) {
                continue;
            }
            String key = entry.substring(0, splitIndex).trim();
            String value = entry.substring(splitIndex + 1).trim();
            mappings.put(key, value);
        }
        return mappings;
    }
}
