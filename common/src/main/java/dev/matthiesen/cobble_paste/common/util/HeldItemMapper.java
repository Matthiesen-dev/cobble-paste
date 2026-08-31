package dev.matthiesen.cobble_paste.common.util;

import com.cobblemon.mod.common.pokemon.helditem.CobblemonHeldItemManager;
import net.minecraft.world.item.Item;

import java.lang.reflect.Field;
import java.util.Map;

public final class HeldItemMapper {
    public static String getShowdownIdForItem(Item item) {
        Map<Item, String> heldItemMappings = getHeldItemMappings();
        return heldItemMappings.get(item);
    }

    public static Item getItemForShowdownId(String showdownId) {
        Map<Item, String> heldItemMappings = getHeldItemMappings();
        for (Map.Entry<Item, String> entry : heldItemMappings.entrySet()) {
            if (entry.getValue().equals(showdownId)) {
                return entry.getKey();
            }
        }
        return null;
    }

    /** Remappings of [Item] to showdownId strings. */
    public static Map<Item, String> getHeldItemMappings() {
        CobblemonHeldItemManager heldItemManager = CobblemonHeldItemManager.INSTANCE;
        return getHeldItems(heldItemManager);
    }

    public static Map<Item, String> getHeldItems(CobblemonHeldItemManager heldItemManager) {
        try {
            Field field = CobblemonHeldItemManager.class.getDeclaredField("remaps");
            field.setAccessible(true);
            @SuppressWarnings("unchecked")
            Map<Item, String> remaps = (Map<Item, String>) field.get(heldItemManager);
            return remaps;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
