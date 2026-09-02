# Cobble Paste

<div>
  <img src="https://mods.matthiesen.dev/badges/matthiesenCore.svg" alt="Matthiesen Core">
  <img src="https://mods.matthiesen.dev/badges/cobblemon.svg" alt="Cobblemon">
</div>

This is a Server-side mod that adds support for importing and exporting Pokémon from [Pokepast.es](https://pokepast.es) into Cobblemon. 
It allows players to easily share their Pokémon with others or import Pokémon from the web into their game.

Note: Importing and exporting Pokémon from Pokepast.es is only available for players with appropriate permissions. Please check with your server administrator if you are unsure about your permissions.

## Requirements

- [Matthiesen Core](https://modrinth.com/mod/matthiesen-core)
- [Cobblemon](https://modrinth.com/mod/cobblemon)
- [Fabric API](https://modrinth.com/mod/fabric-api) (Fabric only)
- [Forge Config API Port](https://modrinth.com/mod/forge-config-api-port) (Fabric only)

### Optional Dependencies

- [GooeyLibs](https://modrinth.com/mod/gooeylibs)

## Quick Start

Cobble Paste is a server-side mod for importing and exporting Cobblemon teams to Pokepast.es.

### Commands

Use these commands from the server console or in-game as a player with the required permissions:

- `/cobble-paste import <url>`: fetches a Pokepast.es URL and imports the team into the caller's party.
- `/cobble-paste preview <url>`: opens a read-only inventory preview of the team when GooeyLibs is installed on the server.
- `/cobble-paste export`: exports the current party to Pokepast.es and returns a shareable link.

### Configuration

Cobble Paste stores its config files in your game directory under `config/cobble_paste/`.

#### `server.toml`

Controls the exported paste author and item name mappings used during import/export.

```toml
[general]
    pasteAuthor = "CobblePaste"
    itemMappings = [
        "Leftovers=cobblemon:leftovers",
        "Choice Band=cobblemon:choice_band",
        "Booster Energy=UNMAPPED:booster_energy"
    ]
```

- `general.pasteAuthor`: the author name shown on Pokepast.es when exporting.
- `general.itemMappings`: a list of `Showdown Item Name=registry_id` pairs.
- Use `UNMAPPED:<name>` to skip unsupported item mappings instead of hard-failing on import/export.

#### `permissions.toml`

Controls which permission levels can run each command.

```toml
[permissions]
    commandPasteImportPermLevel = "ALL_COMMANDS"
    commandPasteExportPermLevel = "ALL_COMMANDS"
    commandPastePreviewPermLevel = "ALL_COMMANDS"
```

These values are enum-based permission levels from the server permissions system.

- `cobble_paste.command.cobble-paste.import`: permission to run the import command.
- `cobble_paste.command.cobble-paste.export`: permission to run the export command.
- `cobble_paste.command.cobble-paste.preview`: permission to run the preview command.

### Cobblemon NPC Party Provider

Cobble Paste also adds a new NPC Party Provider for Cobblemon, allowing NPCs to have their parties defined by Pokepast.es URLs.

> For more information about NPC Presets and Party providers, see the [Cobblemon NPC API documentation](https://gitlab.com/cable-mc/cobblemon/-/blob/1.7.3/common/src/main/kotlin/com/cobblemon/mod/common/api/npc/README.md?ref_type=tags#party)

In your Cobblemon NPC Datapack json, you can use the following format to define an NPC's party:

```json
{
  "party": {
    "type": "cobble_paste:poke_paste",
    "isStatic": true,
    "pokePasteId": "your-paste-id-or-url"
  }
}
```

## Docs

Documentation for this mod can be found at [mods.matthiesen.dev](https://mods.matthiesen.dev/cobble-paste/)

## Version Compatibility

| Minecraft Version | Cobblemon Version | Mod Version |
|-------------------|-------------------|-------------|
| 1.21.1            | 1.7.3             | 1.x.x       |

## FastStats Metrics

This mod uses [FastStats](https://faststats.dev) to collect anonymous usage statistics. This helps the developer understand
how this mod is being used and improve it over time. You can learn more about the data collected and how it is used by visiting
[FastStats: Information](https://faststats.dev/info).

You can also view the data collected by this mod on the [FastStats: Cobble Paste](https://faststats.dev/project/cobble-paste) page.

To opt out of this data collection, set the `enabled` property to `false` in the `<game_directory>/config/matthiesen_core/metrics.properties` file.

## License

MIT - see `LICENSE`.
