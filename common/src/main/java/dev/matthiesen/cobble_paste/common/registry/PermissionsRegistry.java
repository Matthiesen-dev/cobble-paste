package dev.matthiesen.cobble_paste.common.registry;

import dev.matthiesen.cobble_paste.common.CobblePasteCommon;
import dev.matthiesen.cobble_paste.common.config.CobblePasteConfig;
import dev.matthiesen.matthiesen_core.common.api.permissions.Permission;
import dev.matthiesen.matthiesen_core.common.api.permissions.PermissionLevel;
import dev.matthiesen.matthiesen_core.common.utility.AbstractPermission;
import net.minecraft.commands.CommandSourceStack;

public final class PermissionsRegistry {
    public static Permission COMMAND_PASTE_IMPORT;
    public static Permission COMMAND_PASTE_EXPORT;
    public static Permission COMMAND_PASTE_PREVIEW;

    static {
        COMMAND_PASTE_IMPORT = PermissionsRegistry.register("command.cobble-paste.import", CobblePasteConfig.PERMISSIONS_CONFIG.commandPasteImportPermLevel.get().getLevel());
        COMMAND_PASTE_EXPORT = PermissionsRegistry.register("command.cobble-paste.export", CobblePasteConfig.PERMISSIONS_CONFIG.commandPasteExportPermLevel.get().getLevel());
        COMMAND_PASTE_PREVIEW = PermissionsRegistry.register("command.cobble-paste.preview", CobblePasteConfig.PERMISSIONS_CONFIG.commandPastePreviewPermLevel.get().getLevel());
    }

    public static boolean checkPermission(CommandSourceStack source, Permission permission) {
        return CobblePasteCommon.INSTANCE.getPermissionsManager().getPermissionValidator().hasPermission(source, permission);
    }

    public static PermissionLevel toPermLevel(int permLevel) {
        for (PermissionLevel value : PermissionLevel.values()) {
            if (value.ordinal() == permLevel) {
                return value;
            }
        }
        return PermissionLevel.CHEAT_COMMANDS_AND_COMMAND_BLOCKS;
    }

    public static void init() {}

    @SuppressWarnings("SameParameterValue")
    private static Permission register(String node, int level) {
        var newPermission = modPermission(node, toPermLevel(level));
        CobblePasteCommon.INSTANCE.getPermissionsManager().registerPermission(newPermission);
        return newPermission;
    }

    @SuppressWarnings("SameParameterValue")
    private static Permission modPermission(String node, PermissionLevel level) {
        return new AbstractPermission(node, level) {
            @Override
            protected String getModId() {
                return CobblePasteCommon.MOD_ID;
            }

            @Override
            protected String getPermissionNamespace() {
                return "CobblePaste";
            }
        };
    }
}
