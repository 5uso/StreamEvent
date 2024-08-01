package suso.event_base.custom.items;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class CustomItems {
    private static Item register(String id, Item item) {
        return Registry.register(Registries.ITEM, id, item);
    }

    public static void register() {

    }
}
