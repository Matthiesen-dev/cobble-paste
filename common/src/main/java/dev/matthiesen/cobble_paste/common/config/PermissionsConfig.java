package dev.matthiesen.cobble_paste.common.config;

import dev.matthiesen.matthiesen_core.common.api.permissions.PermissionLevel;
import net.neoforged.neoforge.common.ModConfigSpec;

public final class PermissionsConfig {

    public ModConfigSpec.EnumValue<PermissionLevel> commandPasteImportPermLevel;
    public ModConfigSpec.EnumValue<PermissionLevel> commandPasteExportPermLevel;
    public ModConfigSpec.EnumValue<PermissionLevel> commandPastePreviewPermLevel;

    public PermissionsConfig(ModConfigSpec.Builder builder) {
        builder.push("permissions");

        commandPasteImportPermLevel = builder
                .comment("The permission level required to use the /cobble-paste import command.")
                .defineEnum("commandPasteImportPermLevel", PermissionLevel.ALL_COMMANDS);

        commandPasteExportPermLevel = builder
                .comment("The permission level required to use the /cobble-paste export command.")
                .defineEnum("commandPasteExportPermLevel", PermissionLevel.ALL_COMMANDS);

        commandPastePreviewPermLevel = builder
                .comment("The permission level required to use the /cobble-paste preview command.")
                .defineEnum("commandPastePreviewPermLevel", PermissionLevel.ALL_COMMANDS);

        builder.pop();
    }
}
