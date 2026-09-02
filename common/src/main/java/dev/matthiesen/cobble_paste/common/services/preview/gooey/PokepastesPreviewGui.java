package dev.matthiesen.cobble_paste.common.services.preview.gooey;

import ca.landonjw.gooeylibs2.api.UIManager;
import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import ca.landonjw.gooeylibs2.api.button.PlaceholderButton;
import ca.landonjw.gooeylibs2.api.helpers.PaginationHelper;
import ca.landonjw.gooeylibs2.api.page.GooeyPage;
import ca.landonjw.gooeylibs2.api.template.types.ChestTemplate;
import com.cobblemon.mod.common.CobblemonItems;
import com.cobblemon.mod.common.pokemon.Pokemon;
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

import java.util.ArrayList;
import java.util.List;

public final class PokepastesPreviewGui {
    private final ServerPlayer player;
    private final ShowdownTeam team;

    public PokepastesPreviewGui(ServerPlayer player, ShowdownTeam team) {
        this.player = player;
        this.team = team;
    }

    public void open() {
        PlaceholderButton placeholder = new PlaceholderButton();

        ChestTemplate template = ChestTemplate.builder(3)
                .set(1, 1, placeholder)
                .set(1, 2, placeholder)
                .set(1, 3, placeholder)
                .set(1, 5, placeholder)
                .set(1, 6, placeholder)
                .set(1, 7, placeholder)
                .set(2, 4, importButton())
                .fill(frameButton())
                .build();

        List<Button> buttons = new ArrayList<>();

        for (int index = 0; index < team.team().size(); index++) {
            Pokemon pokemon = team.team().get(index).toPokemon();
            ItemStack display = new PokeUtil(pokemon).toItem();
            buttons.add(GooeyButton.builder().display(display).build());
        }

        GooeyPage page = PaginationHelper.createPagesFromPlaceholders(template, buttons, null);
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
        ItemStack display = new ItemBuilder(new ItemStack(CobblemonItems.POKE_BALL))
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
}
