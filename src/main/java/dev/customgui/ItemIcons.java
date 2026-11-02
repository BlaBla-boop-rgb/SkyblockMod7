package dev.customgui;

import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Findet zu einem Produktnamen (z. B. "Magmacreme") ein Item zum Anzeigen. Zuerst wird das
 * Inventar durchsucht (findet auch Server-Items mit eigenem Namen), danach alle Vanilla-Items.
 */
public final class ItemIcons {

    private static Map<String, ItemStack> index;

    private ItemIcons() {
    }

    public static ItemStack find(Minecraft mc, String product) {
        String key = norm(product);

        if (mc.player != null) {
            Inventory inv = mc.player.getInventory();
            for (int i = 0; i < inv.getContainerSize(); i++) {
                ItemStack st = inv.getItem(i);
                if (!st.isEmpty() && norm(st.getHoverName().getString()).equals(key)) {
                    return st.copyWithCount(1);
                }
            }
        }

        if (index == null) {
            index = new HashMap<>();
            for (Item item : BuiltInRegistries.ITEM) {
                ItemStack st = item.getDefaultInstance();
                if (st.isEmpty()) continue;
                index.putIfAbsent(norm(st.getHoverName().getString()), st);
            }
        }

        ItemStack hit = index.get(key);
        if (hit == null && key.endsWith("n")) hit = index.get(key.substring(0, key.length() - 1)); // Kerzen -> Kerze
        if (hit == null && key.endsWith("en")) hit = index.get(key.substring(0, key.length() - 2));
        if (hit == null && key.endsWith("s")) hit = index.get(key.substring(0, key.length() - 1));
        return hit;
    }

    private static String norm(String s) {
        return s.trim().toLowerCase(Locale.ROOT);
    }
}
