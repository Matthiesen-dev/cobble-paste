package dev.matthiesen.cobble_paste.common.services.gooey;

import ca.landonjw.gooeylibs2.api.UIManager;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import ca.landonjw.gooeylibs2.api.page.GooeyPage;
import ca.landonjw.gooeylibs2.api.template.types.ChestTemplate;
import com.cobblemon.mod.common.CobblemonItems;
import com.cobblemon.mod.common.pokemon.Pokemon;
import dev.matthiesen.cobble_paste.common.converter.ShowdownToCobblemonConverter;
import dev.matthiesen.cobble_paste.common.formats.ShowdownTeam;
import dev.matthiesen.cobble_paste.common.registry.PermissionsRegistry;
import dev.matthiesen.cobble_paste.common.services.TeamImportService;
import dev.matthiesen.cobble_paste.common.util.PokeUtil;
import dev.matthiesen.matthiesen_core.common.utility.item.ItemBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public final class PokepastesPreviewGui {
    private static final int[] TEAM_SLOTS = {10, 11, 12, 14, 15, 16};

    private final ServerPlayer player;
    private final ShowdownTeam team;

    public PokepastesPreviewGui(ServerPlayer player, ShowdownTeam team) {
        this.player = player;
        this.team = team;
    }

    public void open() {
        ChestTemplate.Builder template = ChestTemplate.builder(3);
        for (int slot = 0; slot < 27; slot++) {
            template.set(slot / 9, slot % 9, frameButton());
        }

        int count = Math.min(team.team().size(), TEAM_SLOTS.length);
        for (int index = 0; index < count; index++) {
            Pokemon pokemon = ShowdownToCobblemonConverter.convert(team.team().get(index));
            ItemStack display = pokemon == null ? invalidPokemonItem(team.team().get(index).species()) : new PokeUtil(pokemon).toItem();
            int slot = TEAM_SLOTS[index];
            template.set(slot / 9, slot % 9, GooeyButton.builder().display(display).build());
        }
        template.set(2, 4, importButton());

        GooeyPage page = GooeyPage.builder().template(template.build()).build();
        page.setTitle(Component.literal("Pokepast.es Preview"));
        UIManager.openUIForcefully(player, page);
    }

    private static GooeyButton frameButton() {
        ItemStack frame = new ItemBuilder(new ItemStack(Items.GRAY_STAINED_GLASS_PANE))
                .hideAdditional()
                .setCustomName(Component.literal(" "))
                .build();
        return GooeyButton.builder().display(frame).build();
    }

    private GooeyButton importButton() {
        ItemStack display = new ItemBuilder(new ItemStack(Items.LIME_DYE))
                .hideAdditional()
                .setCustomName(Component.literal("Import Team").withStyle(ChatFormatting.GREEN))
                .addLore(new Component[]{
                        Component.literal("Moves your current party to your PC.").withStyle(ChatFormatting.GRAY)
                })
                .build();
        return GooeyButton.builder()
                .display(display)
                .onClick(() -> {
                    UIManager.closeUI(player);
                    player.server.execute(() -> {
                        if (!PermissionsRegistry.checkPermission(
                                player.createCommandSourceStack(),
                                PermissionsRegistry.COMMAND_PASTE_IMPORT
                        )) {
                            player.sendSystemMessage(Component.literal("You do not have permission to import teams.")
                                    .withStyle(ChatFormatting.RED));
                            return;
                        }
                        TeamImportService.importIntoParty(player, team);
                    });
                })
                .build();
    }

    private static ItemStack invalidPokemonItem(String species) {
        return new ItemBuilder(new ItemStack(CobblemonItems.POKE_BALL))
                .hideAdditional()
                .setCustomName(Component.literal("Could not preview " + species).withStyle(ChatFormatting.RED))
                .build();
    }
}
