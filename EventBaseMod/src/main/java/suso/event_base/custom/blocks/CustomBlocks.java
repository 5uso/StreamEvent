package suso.event_base.custom.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.block.StainedGlassBlock;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.DyeColor;
import net.minecraft.util.registry.Registry;

public class CustomBlocks {
    public static Block GRAY_HOLOBLOCK;
    public static Block WHITE_HOLOBLOCK;
    public static Block PINK_HOLOBLOCK;
    public static Block PURPLE_HOLOBLOCK;
    public static Block BLUE_HOLOBLOCK;
    public static Block CYAN_HOLOBLOCK;
    public static Block LIGHT_BLUE_HOLOBLOCK;
    public static Block GREEN_HOLOBLOCK;
    public static Block LIME_HOLOBLOCK;
    public static Block YELLOW_HOLOBLOCK;
    public static Block ORANGE_HOLOBLOCK;
    public static Block RED_HOLOBLOCK;
    public static Block GRAY_GUNK;
    public static Block WHITE_GUNK;
    public static Block PINK_GUNK;
    public static Block PURPLE_GUNK;
    public static Block BLUE_GUNK;
    public static Block CYAN_GUNK;
    public static Block LIGHT_BLUE_GUNK;
    public static Block GREEN_GUNK;
    public static Block LIME_GUNK;
    public static Block YELLOW_GUNK;
    public static Block ORANGE_GUNK;
    public static Block RED_GUNK;

    private static Block register(String id, Block block) {
        return Registry.register(Registry.BLOCK, id, block);
    }

    public static void register() {
        GRAY_HOLOBLOCK = register("suso:gray_holoblock", new StainedGlassBlock(DyeColor.GRAY, FabricBlockSettings.of(Material.AMETHYST, MapColor.GRAY).sounds(BlockSoundGroup.AMETHYST_BLOCK).nonOpaque()));
        WHITE_HOLOBLOCK = register("suso:white_holoblock", new StainedGlassBlock(DyeColor.WHITE, FabricBlockSettings.of(Material.AMETHYST, MapColor.WHITE).sounds(BlockSoundGroup.AMETHYST_BLOCK).nonOpaque()));
        PINK_HOLOBLOCK = register("suso:pink_holoblock", new StainedGlassBlock(DyeColor.PINK, FabricBlockSettings.of(Material.AMETHYST, MapColor.PINK).sounds(BlockSoundGroup.AMETHYST_BLOCK).nonOpaque()));
        PURPLE_HOLOBLOCK = register("suso:purple_holoblock", new StainedGlassBlock(DyeColor.PURPLE, FabricBlockSettings.of(Material.AMETHYST, MapColor.PURPLE).sounds(BlockSoundGroup.AMETHYST_BLOCK).nonOpaque()));
        BLUE_HOLOBLOCK = register("suso:blue_holoblock", new StainedGlassBlock(DyeColor.BLUE, FabricBlockSettings.of(Material.AMETHYST, MapColor.BLUE).sounds(BlockSoundGroup.AMETHYST_BLOCK).nonOpaque()));
        CYAN_HOLOBLOCK = register("suso:cyan_holoblock", new StainedGlassBlock(DyeColor.CYAN, FabricBlockSettings.of(Material.AMETHYST, MapColor.CYAN).sounds(BlockSoundGroup.AMETHYST_BLOCK).nonOpaque()));
        LIGHT_BLUE_HOLOBLOCK = register("suso:light_blue_holoblock", new StainedGlassBlock(DyeColor.LIGHT_BLUE, FabricBlockSettings.of(Material.AMETHYST, MapColor.LIGHT_BLUE).sounds(BlockSoundGroup.AMETHYST_BLOCK).nonOpaque()));
        GREEN_HOLOBLOCK = register("suso:green_holoblock", new StainedGlassBlock(DyeColor.GREEN, FabricBlockSettings.of(Material.AMETHYST, MapColor.GREEN).sounds(BlockSoundGroup.AMETHYST_BLOCK).nonOpaque()));
        LIME_HOLOBLOCK = register("suso:lime_holoblock", new StainedGlassBlock(DyeColor.LIME, FabricBlockSettings.of(Material.AMETHYST, MapColor.LIME).sounds(BlockSoundGroup.AMETHYST_BLOCK).nonOpaque()));
        YELLOW_HOLOBLOCK = register("suso:yellow_holoblock", new StainedGlassBlock(DyeColor.YELLOW, FabricBlockSettings.of(Material.AMETHYST, MapColor.YELLOW).sounds(BlockSoundGroup.AMETHYST_BLOCK).nonOpaque()));
        ORANGE_HOLOBLOCK = register("suso:orange_holoblock", new StainedGlassBlock(DyeColor.ORANGE, FabricBlockSettings.of(Material.AMETHYST, MapColor.ORANGE).sounds(BlockSoundGroup.AMETHYST_BLOCK).nonOpaque()));
        RED_HOLOBLOCK = register("suso:red_holoblock", new StainedGlassBlock(DyeColor.RED, FabricBlockSettings.of(Material.AMETHYST, MapColor.RED).sounds(BlockSoundGroup.AMETHYST_BLOCK).nonOpaque()));

        GRAY_GUNK = register("suso:gray_gunk", new Block(FabricBlockSettings.of(Material.ORGANIC_PRODUCT, MapColor.GRAY).slipperiness(0.99F).sounds(BlockSoundGroup.SLIME)));
        WHITE_GUNK = register("suso:white_gunk", new Block(FabricBlockSettings.of(Material.ORGANIC_PRODUCT, MapColor.WHITE).slipperiness(0.99F).sounds(BlockSoundGroup.SLIME)));
        PINK_GUNK = register("suso:pink_gunk", new Block(FabricBlockSettings.of(Material.ORGANIC_PRODUCT, MapColor.PINK).slipperiness(0.99F).sounds(BlockSoundGroup.SLIME)));
        PURPLE_GUNK = register("suso:purple_gunk", new Block(FabricBlockSettings.of(Material.ORGANIC_PRODUCT, MapColor.PURPLE).slipperiness(0.99F).sounds(BlockSoundGroup.SLIME)));
        BLUE_GUNK = register("suso:blue_gunk", new Block(FabricBlockSettings.of(Material.ORGANIC_PRODUCT, MapColor.BLUE).slipperiness(0.99F).sounds(BlockSoundGroup.SLIME)));
        CYAN_GUNK = register("suso:cyan_gunk", new Block(FabricBlockSettings.of(Material.ORGANIC_PRODUCT, MapColor.CYAN).slipperiness(0.99F).sounds(BlockSoundGroup.SLIME)));
        LIGHT_BLUE_GUNK = register("suso:light_blue_gunk", new Block(FabricBlockSettings.of(Material.ORGANIC_PRODUCT, MapColor.LIGHT_BLUE).slipperiness(0.99F).sounds(BlockSoundGroup.SLIME)));
        GREEN_GUNK = register("suso:green_gunk", new Block(FabricBlockSettings.of(Material.ORGANIC_PRODUCT, MapColor.GREEN).slipperiness(0.99F).sounds(BlockSoundGroup.SLIME)));
        LIME_GUNK = register("suso:lime_gunk", new Block(FabricBlockSettings.of(Material.ORGANIC_PRODUCT, MapColor.LIME).slipperiness(0.99F).sounds(BlockSoundGroup.SLIME)));
        YELLOW_GUNK = register("suso:yellow_gunk", new Block(FabricBlockSettings.of(Material.ORGANIC_PRODUCT, MapColor.YELLOW).slipperiness(0.99F).sounds(BlockSoundGroup.SLIME)));
        ORANGE_GUNK = register("suso:orange_gunk", new Block(FabricBlockSettings.of(Material.ORGANIC_PRODUCT, MapColor.ORANGE).slipperiness(0.99F).sounds(BlockSoundGroup.SLIME)));
        RED_GUNK = register("suso:red_gunk", new Block(FabricBlockSettings.of(Material.ORGANIC_PRODUCT, MapColor.RED).slipperiness(0.99F).sounds(BlockSoundGroup.SLIME)));
    }
}
