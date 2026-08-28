package dev.matthiesen.cobble_paste.common.config;

import dev.matthiesen.matthiesen_core.common.api.permissions.PermissionLevel;
import net.neoforged.neoforge.common.ModConfigSpec;

public final class PermissionsConfig {

    public ModConfigSpec.EnumValue<PermissionLevel> commandPasteImportPermLevel;
    public ModConfigSpec.EnumValue<PermissionLevel> commandPasteExportPermLevel;

    public PermissionsConfig(ModConfigSpec.Builder builder) {
        builder.push("permissions");

        commandPasteImportPermLevel = builder
                .comment("The permission level required to use the /cobble-paste import command.")
                .defineEnum("commandPasteImportPermLevel", PermissionLevel.CHEAT_COMMANDS_AND_COMMAND_BLOCKS);

        commandPasteExportPermLevel = builder
                .comment("The permission level required to use the /cobble-paste export command.")
                .defineEnum("commandPasteExportPermLevel", PermissionLevel.CHEAT_COMMANDS_AND_COMMAND_BLOCKS);

        builder.pop();
    }
}
