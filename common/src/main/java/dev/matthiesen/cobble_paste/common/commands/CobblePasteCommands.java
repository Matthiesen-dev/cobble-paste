package dev.matthiesen.cobble_paste.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.util.PlayerExtensionsKt;
import dev.matthiesen.cobble_paste.common.api.PokePasteApiClient;
import dev.matthiesen.cobble_paste.common.config.CobblePasteConfig;
import dev.matthiesen.cobble_paste.common.converter.CobblemonToShowdownConverter;
import dev.matthiesen.cobble_paste.common.converter.ShowdownToCobblemonConverter;
import dev.matthiesen.cobble_paste.common.formats.ShowdownTeam;
import dev.matthiesen.cobble_paste.common.parser.PokePasteParser;
import dev.matthiesen.cobble_paste.common.registry.PermissionsRegistry;
import dev.matthiesen.cobble_paste.common.serializer.PokePasteSerializer;
import dev.matthiesen.matthiesen_core.common.api.command.CoreCommand;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;

public final class CobblePasteCommands implements CoreCommand {
    public static final CobblePasteCommands CMD = new CobblePasteCommands();

    private CobblePasteCommands() {
    }

    @Override
    public void register(CommandDispatcher<CommandSourceStack> commandDispatcher, CommandBuildContext commandBuildContext, Commands.CommandSelection commandSelection) {
        var importSubCommand = Commands.literal("import")
                .requires(source -> PermissionsRegistry.checkPermission(source, PermissionsRegistry.COMMAND_PASTE_IMPORT))
                .then(Commands.argument("url", StringArgumentType.string())
                        .executes(CobblePasteCommands::importPaste));

        var exportSubCommand = Commands.literal("export")
                .requires(source -> PermissionsRegistry.checkPermission(source, PermissionsRegistry.COMMAND_PASTE_EXPORT))
                .executes(CobblePasteCommands::exportPaste);

        LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal("cobble-paste")
                .then(importSubCommand)
                .then(exportSubCommand);

        commandDispatcher.register(command);
    }

    private static int importPaste(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        String rawUrl = StringArgumentType.getString(context, "url");
        context.getSource().sendSystemMessage(Component.literal("Fetching Pokepaste...").withStyle(ChatFormatting.YELLOW));

        PokePasteApiClient.fetchRawPaste(rawUrl)
                .thenAccept(raw -> {
                    try {
                        ShowdownTeam team = PokePasteParser.parse(raw);
                        if (team.team().isEmpty()) {
                            player.sendSystemMessage(Component.literal("No valid Pokémon were found in that paste.").withStyle(ChatFormatting.RED));
                            return;
                        }

                        player.server.execute(() -> importTeamIntoParty(player, team));
                    } catch (Exception ex) {
                        player.sendSystemMessage(Component.literal("Failed to parse Pokepaste: " + ex.getMessage()).withStyle(ChatFormatting.RED));
                    }
                })
                .exceptionally(throwable -> {
                    player.sendSystemMessage(Component.literal("Could not fetch Pokepaste: " + throwable.getMessage()).withStyle(ChatFormatting.RED));
                    return null;
                });
        return 1;
    }

    @SuppressWarnings("SameReturnValue")
    private static int exportPaste(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        var party = PlayerExtensionsKt.party(player);
        if (party.isEmpty()) {
            player.sendSystemMessage(Component.literal("Your party is empty.").withStyle(ChatFormatting.RED));
            return 1;
        }

        List<dev.matthiesen.cobble_paste.common.formats.ShowdownEntry> convertedEntries = new ArrayList<>();
        for (int i = 0; i < party.size(); i++) {
            var converted = CobblemonToShowdownConverter.convert(party.get(i));
            if (converted != null) {
                convertedEntries.add(converted);
            }
        }

        ShowdownTeam team = new ShowdownTeam("", convertedEntries);

        String serialized = PokePasteSerializer.serialize(team);
        String author = CobblePasteConfig.getPasteAuthor();
        PokePasteApiClient.createPaste(serialized, "Cobble Paste export", author)
                .thenAccept(url -> {
                    Component link = Component.literal(url)
                            .withStyle(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url))
                                    .withUnderlined(true));
                    player.sendSystemMessage(Component.literal("Exported party to Pokepast.es:")
                            .withStyle(ChatFormatting.GREEN)
                            .append(" ")
                            .append(link));
                })
                .exceptionally(throwable -> {
                    player.sendSystemMessage(Component.literal("Failed to export party: " + throwable.getMessage()).withStyle(ChatFormatting.RED));
                    return null;
                });

        return 1;
    }

    private static void importTeamIntoParty(ServerPlayer player, ShowdownTeam team) {
        var party = PlayerExtensionsKt.party(player);
        var pc = PlayerExtensionsKt.pc(player);

        List<Pokemon> movedToPc = new ArrayList<>();
        for (int i = 0; i < party.size(); i++) {
            Pokemon current = party.get(i);
            if (current != null) {
                movedToPc.add(current);
            }
        }
        for (Pokemon pokemon : movedToPc) {
            party.remove(pokemon);
            pc.add(pokemon);
        }

        int inserted = 0;
        for (var entry : team.team()) {
            if (inserted >= 6) {
                break;
            }
            Pokemon converted = ShowdownToCobblemonConverter.convert(entry);
            if (converted == null) {
                continue;
            }
            if (party.size() >= 6) {
                pc.add(converted);
                continue;
            }
            party.add(converted);
            inserted++;
        }

        player.sendSystemMessage(Component.literal("Imported " + inserted + " Pokémon into your party.").withStyle(ChatFormatting.GREEN));
    }
}
