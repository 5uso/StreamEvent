package suso.event_manage.custom.items;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registry;
import suso.event_manage.custom.blocks.CustomBlocks;

public class CustomItems {
    public static Item GRAY_HOLOBLOCK;
    public static Item WHITE_HOLOBLOCK;
    public static Item PINK_HOLOBLOCK;
    public static Item PURPLE_HOLOBLOCK;
    public static Item BLUE_HOLOBLOCK;
    public static Item CYAN_HOLOBLOCK;
    public static Item LIGHT_BLUE_HOLOBLOCK;
    public static Item GREEN_HOLOBLOCK;
    public static Item LIME_HOLOBLOCK;
    public static Item YELLOW_HOLOBLOCK;
    public static Item ORANGE_HOLOBLOCK;
    public static Item RED_HOLOBLOCK;

    private static Item register(String id, Item item) {
        return Registry.register(Registry.ITEM, id, item);
    }

    public static void register() {
        GRAY_HOLOBLOCK = register("suso:gray_holoblock", new BlockItem(CustomBlocks.GRAY_HOLOBLOCK, new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS)));
        WHITE_HOLOBLOCK = register("suso:white_holoblock", new BlockItem(CustomBlocks.WHITE_HOLOBLOCK, new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS)));
        PINK_HOLOBLOCK = register("suso:pink_holoblock", new BlockItem(CustomBlocks.PINK_HOLOBLOCK, new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS)));
        PURPLE_HOLOBLOCK = register("suso:purple_holoblock", new BlockItem(CustomBlocks.PURPLE_HOLOBLOCK, new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS)));
        BLUE_HOLOBLOCK = register("suso:blue_holoblock", new BlockItem(CustomBlocks.BLUE_HOLOBLOCK, new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS)));
        CYAN_HOLOBLOCK = register("suso:cyan_holoblock", new BlockItem(CustomBlocks.CYAN_HOLOBLOCK, new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS)));
        LIGHT_BLUE_HOLOBLOCK = register("suso:light_blue_holoblock", new BlockItem(CustomBlocks.LIGHT_BLUE_HOLOBLOCK, new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS)));
        GREEN_HOLOBLOCK = register("suso:green_holoblock", new BlockItem(CustomBlocks.GREEN_HOLOBLOCK, new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS)));
        LIME_HOLOBLOCK = register("suso:lime_holoblock", new BlockItem(CustomBlocks.LIME_HOLOBLOCK, new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS)));
        YELLOW_HOLOBLOCK = register("suso:yellow_holoblock", new BlockItem(CustomBlocks.YELLOW_HOLOBLOCK, new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS)));
        ORANGE_HOLOBLOCK = register("suso:orange_holoblock", new BlockItem(CustomBlocks.ORANGE_HOLOBLOCK, new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS)));
        RED_HOLOBLOCK = register("suso:red_holoblock", new BlockItem(CustomBlocks.RED_HOLOBLOCK, new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS)));
    }
}
