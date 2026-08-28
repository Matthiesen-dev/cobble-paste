package dev.matthiesen.cobble_paste.common.config;

import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.ArrayList;
import java.util.List;

public final class ServerConfig {
    public ModConfigSpec.ConfigValue<String> PASTE_AUTHOR;
    public ModConfigSpec.ConfigValue<List<? extends String>> ITEM_MAPPINGS;

    public ServerConfig(ModConfigSpec.Builder builder) {
        builder
                .comment("Author name to display on pokepast.es when exporting.")
                .define("general.pasteAuthor", "CobblePaste");

        builder
                .comment("Showdown item name to registry id mapping. Use UNMAPPED:prefix to skip unsupported items.")
                .defineList("item_mappings.entries", defaultItemMappings(), () -> "", value -> value instanceof String);
    }

    private static List<String> defaultItemMappings() {
        List<String> defaults = new ArrayList<>();
        defaults.add("Leftovers=cobblemon:leftovers");
        defaults.add("Choice Band=cobblemon:choice_band");
        defaults.add("Choice Specs=cobblemon:choice_specs");
        defaults.add("Choice Scarf=cobblemon:choice_scarf");
        defaults.add("Life Orb=cobblemon:life_orb");
        defaults.add("Assault Vest=cobblemon:assault_vest");
        defaults.add("Rocky Helmet=cobblemon:rocky_helmet");
        defaults.add("Focus Sash=cobblemon:focus_sash");
        defaults.add("Eviolite=cobblemon:eviolite");
        defaults.add("Heavy-Duty Boots=cobblemon:heavy_duty_boots");
        defaults.add("Black Sludge=cobblemon:black_sludge");
        defaults.add("Weakness Policy=cobblemon:weakness_policy");
        defaults.add("Safety Goggles=cobblemon:safety_goggles");
        defaults.add("Air Balloon=cobblemon:air_balloon");
        defaults.add("Throat Spray=cobblemon:throat_spray");
        defaults.add("Clear Amulet=cobblemon:clear_amulet");
        defaults.add("Loaded Dice=cobblemon:loaded_dice");
        defaults.add("Covert Cloak=cobblemon:covert_cloak");
        defaults.add("Mirror Herb=cobblemon:mirror_herb");
        defaults.add("Shed Shell=cobblemon:shed_shell");
        defaults.add("Iron Ball=cobblemon:iron_ball");
        defaults.add("Toxic Orb=cobblemon:toxic_orb");
        defaults.add("Flame Orb=cobblemon:flame_orb");
        defaults.add("Expert Belt=cobblemon:expert_belt");
        defaults.add("White Herb=cobblemon:white_herb");
        defaults.add("Power Herb=cobblemon:power_herb");
        defaults.add("Mental Herb=cobblemon:mental_herb");
        defaults.add("Bright Powder=cobblemon:bright_powder");
        defaults.add("Wide Lens=cobblemon:wide_lens");
        defaults.add("Zoom Lens=cobblemon:zoom_lens");
        defaults.add("Scope Lens=cobblemon:scope_lens");
        defaults.add("Metronome=cobblemon:metronome");
        defaults.add("Charcoal=cobblemon:charcoal_stick");
        defaults.add("Sitrus Berry=cobblemon:sitrus_berry");
        defaults.add("Lum Berry=cobblemon:lum_berry");
        defaults.add("Oran Berry=cobblemon:oran_berry");
        defaults.add("Chesto Berry=cobblemon:chesto_berry");
        defaults.add("Pecha Berry=cobblemon:pecha_berry");
        defaults.add("Rawst Berry=cobblemon:rawst_berry");
        defaults.add("Aspear Berry=cobblemon:aspear_berry");
        defaults.add("Leppa Berry=cobblemon:leppa_berry");
        defaults.add("Persim Berry=cobblemon:persim_berry");
        defaults.add("Cheri Berry=cobblemon:cheri_berry");
        defaults.add("Booster Energy=UNMAPPED:booster_energy");
        defaults.add("Normalium Z=UNMAPPED:normalium_z");
        defaults.add("Firium Z=UNMAPPED:firium_z");
        defaults.add("Waterium Z=UNMAPPED:waterium_z");
        defaults.add("Electrium Z=UNMAPPED:electrium_z");
        defaults.add("Grassium Z=UNMAPPED:grassium_z");
        defaults.add("Icium Z=UNMAPPED:icium_z");
        defaults.add("Fightinium Z=UNMAPPED:fightinium_z");
        defaults.add("Poisonium Z=UNMAPPED:poisonium_z");
        defaults.add("Groundium Z=UNMAPPED:groundium_z");
        defaults.add("Flyinium Z=UNMAPPED:flyinium_z");
        defaults.add("Psychium Z=UNMAPPED:psychium_z");
        defaults.add("Buginium Z=UNMAPPED:buginium_z");
        defaults.add("Rockium Z=UNMAPPED:rockium_z");
        defaults.add("Ghostium Z=UNMAPPED:ghostium_z");
        defaults.add("Dragonium Z=UNMAPPED:dragonium_z");
        defaults.add("Darkinium Z=UNMAPPED:darkinium_z");
        defaults.add("Steelium Z=UNMAPPED:steelium_z");
        defaults.add("Fairium Z=UNMAPPED:fairium_z");
        defaults.add("Pikashunium Z=UNMAPPED:pikashunium_z");
        defaults.add("Aloraichium Z=UNMAPPED:aloraichium_z");
        defaults.add("Snorlium Z=UNMAPPED:snorlium_z");
        defaults.add("Eevium Z=UNMAPPED:eevium_z");
        defaults.add("Mewnium Z=UNMAPPED:mewnium_z");
        defaults.add("Charizardite X=UNMAPPED:charizardite_x");
        defaults.add("Charizardite Y=UNMAPPED:charizardite_y");
        defaults.add("Venusaurite=UNMAPPED:venusaurite");
        defaults.add("Blastoisinite=UNMAPPED:blastoisinite");
        defaults.add("Gengarite=UNMAPPED:gengarite");
        defaults.add("Kangaskhanite=UNMAPPED:kangaskhanite");
        defaults.add("Pinsirite=UNMAPPED:pinsirite");
        defaults.add("Gyaradosite=UNMAPPED:gyaradosite");
        defaults.add("Aerodactylite=UNMAPPED:aerodactylite");
        defaults.add("Mewtwonite X=UNMAPPED:mewtwonite_x");
        defaults.add("Mewtwonite Y=UNMAPPED:mewtwonite_y");
        defaults.add("Scizorite=UNMAPPED:scizorite");
        defaults.add("Heracronite=UNMAPPED:heracronite");
        defaults.add("Houndoominite=UNMAPPED:houndoominite");
        defaults.add("Tyranitarite=UNMAPPED:tyranitarite");
        defaults.add("Blazikenite=UNMAPPED:blazikenite");
        defaults.add("Gardevoirite=UNMAPPED:gardevoirite");
        defaults.add("Mawilite=UNMAPPED:mawilite");
        defaults.add("Aggronite=UNMAPPED:aggronite");
        defaults.add("Medichamite=UNMAPPED:medichamite");
        defaults.add("Manectite=UNMAPPED:manectite");
        defaults.add("Banettite=UNMAPPED:banettite");
        defaults.add("Absolite=UNMAPPED:absolite");
        defaults.add("Salamencite=UNMAPPED:salamencite");
        defaults.add("Metagrossite=UNMAPPED:metagrossite");
        defaults.add("Latiasite=UNMAPPED:latiasite");
        defaults.add("Latiosite=UNMAPPED:latiosite");
        defaults.add("Lopunnite=UNMAPPED:lopunnite");
        defaults.add("Garchompite=UNMAPPED:garchompite");
        defaults.add("Lucarionite=UNMAPPED:lucarionite");
        defaults.add("Abomasite=UNMAPPED:abomasite");
        defaults.add("Galladite=UNMAPPED:galladite");
        defaults.add("Audinite=UNMAPPED:audinite");
        defaults.add("Diancite=UNMAPPED:diancite");
        defaults.add("Sharpedonite=UNMAPPED:sharpedonite");
        defaults.add("Glalitite=UNMAPPED:glalitite");
        defaults.add("Cameruptite=UNMAPPED:cameruptite");
        defaults.add("Altarianite=UNMAPPED:altarianite");
        defaults.add("Swampertite=UNMAPPED:swampertite");
        defaults.add("Sceptilite=UNMAPPED:sceptilite");
        return defaults;
    }
}
