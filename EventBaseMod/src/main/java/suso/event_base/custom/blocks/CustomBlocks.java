package suso.event_base.custom.blocks;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.DyeColor;
import suso.event_base.custom.blocks.entity.*;

public class CustomBlocks {
    public static Block GRAY_HOLOBLOCK, WHITE_HOLOBLOCK, PINK_HOLOBLOCK, PURPLE_HOLOBLOCK, BLUE_HOLOBLOCK, CYAN_HOLOBLOCK, LIGHT_BLUE_HOLOBLOCK, GREEN_HOLOBLOCK, LIME_HOLOBLOCK, YELLOW_HOLOBLOCK, ORANGE_HOLOBLOCK, RED_HOLOBLOCK;
    public static Block GRAY_EMP, WHITE_EMP, PINK_EMP, PURPLE_EMP, BLUE_EMP, CYAN_EMP, LIGHT_BLUE_EMP, GREEN_EMP, LIME_EMP, YELLOW_EMP, ORANGE_EMP, RED_EMP;
    public static Block GRAY_GUNK, WHITE_GUNK, PINK_GUNK, PURPLE_GUNK, BLUE_GUNK, CYAN_GUNK, LIGHT_BLUE_GUNK, GREEN_GUNK, LIME_GUNK, YELLOW_GUNK, ORANGE_GUNK, RED_GUNK;
    public static BlockEntityType<GunkBlockEntity> GUNK_ENTITY;

    public static Block PRIMATICA_POWERUP;
    public static BlockEntityType<PrimaticaPowerupBlockEntity> PRIMATICA_POWERUP_ENTITY;
    public static Block PRIMATICA_RESPAWN;
    public static BlockEntityType<PrimaticaRespawnBlockEntity> PRIMATICA_RESPAWN_ENTITY;
    public static Block PRIMATICA_DOOR;
    public static BlockEntityType<PrimaticaDoorBlockEntity> PRIMATICA_DOOR_ENTITY;

    private static Block register(String id, Block block, boolean registerItem) {
        if(registerItem) {
            BlockItem item = new BlockItem(block, new Item.Settings());
            Registry.register(Registries.ITEM, id, item);
        }

        return Registry.register(Registries.BLOCK, id, block);
    }

    private static Block register(String id, Block block) {
        return register(id, block, true);
    }

    private static <T extends BlockEntity> BlockEntityType<T> register(String id, BlockEntityType<T> blockEntity) {
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, id, blockEntity);
    }

    public static void register() {
        GRAY_HOLOBLOCK = register("suso:gray_holoblock", new StainedGlassBlock(DyeColor.GRAY, AbstractBlock.Settings.create().mapColor(MapColor.GRAY).sounds(BlockSoundGroup.AMETHYST_BLOCK).nonOpaque()));
        WHITE_HOLOBLOCK = register("suso:white_holoblock", new StainedGlassBlock(DyeColor.WHITE, AbstractBlock.Settings.create().mapColor(MapColor.WHITE).sounds(BlockSoundGroup.AMETHYST_BLOCK).nonOpaque()));
        PINK_HOLOBLOCK = register("suso:pink_holoblock", new StainedGlassBlock(DyeColor.PINK, AbstractBlock.Settings.create().mapColor(MapColor.PINK).sounds(BlockSoundGroup.AMETHYST_BLOCK).nonOpaque()));
        PURPLE_HOLOBLOCK = register("suso:purple_holoblock", new StainedGlassBlock(DyeColor.PURPLE, AbstractBlock.Settings.create().mapColor(MapColor.PURPLE).sounds(BlockSoundGroup.AMETHYST_BLOCK).nonOpaque()));
        BLUE_HOLOBLOCK = register("suso:blue_holoblock", new StainedGlassBlock(DyeColor.BLUE, AbstractBlock.Settings.create().mapColor(MapColor.BLUE).sounds(BlockSoundGroup.AMETHYST_BLOCK).nonOpaque()));
        CYAN_HOLOBLOCK = register("suso:cyan_holoblock", new StainedGlassBlock(DyeColor.CYAN, AbstractBlock.Settings.create().mapColor(MapColor.CYAN).sounds(BlockSoundGroup.AMETHYST_BLOCK).nonOpaque()));
        LIGHT_BLUE_HOLOBLOCK = register("suso:light_blue_holoblock", new StainedGlassBlock(DyeColor.LIGHT_BLUE, AbstractBlock.Settings.create().mapColor(MapColor.LIGHT_BLUE).sounds(BlockSoundGroup.AMETHYST_BLOCK).nonOpaque()));
        GREEN_HOLOBLOCK = register("suso:green_holoblock", new StainedGlassBlock(DyeColor.GREEN, AbstractBlock.Settings.create().mapColor(MapColor.GREEN).sounds(BlockSoundGroup.AMETHYST_BLOCK).nonOpaque()));
        LIME_HOLOBLOCK = register("suso:lime_holoblock", new StainedGlassBlock(DyeColor.LIME, AbstractBlock.Settings.create().mapColor(MapColor.LIME).sounds(BlockSoundGroup.AMETHYST_BLOCK).nonOpaque()));
        YELLOW_HOLOBLOCK = register("suso:yellow_holoblock", new StainedGlassBlock(DyeColor.YELLOW, AbstractBlock.Settings.create().mapColor(MapColor.YELLOW).sounds(BlockSoundGroup.AMETHYST_BLOCK).nonOpaque()));
        ORANGE_HOLOBLOCK = register("suso:orange_holoblock", new StainedGlassBlock(DyeColor.ORANGE, AbstractBlock.Settings.create().mapColor(MapColor.ORANGE).sounds(BlockSoundGroup.AMETHYST_BLOCK).nonOpaque()));
        RED_HOLOBLOCK = register("suso:red_holoblock", new StainedGlassBlock(DyeColor.RED, AbstractBlock.Settings.create().mapColor(MapColor.RED).sounds(BlockSoundGroup.AMETHYST_BLOCK).nonOpaque()));

        GRAY_EMP = register("suso:gray_emp", new EMPBlock(AbstractBlock.Settings.create().strength(-1.0F, 3600000.0F).dropsNothing().nonOpaque().noCollision()), false);
        WHITE_EMP = register("suso:white_emp", new EMPBlock(AbstractBlock.Settings.create().strength(-1.0F, 3600000.0F).dropsNothing().nonOpaque().noCollision()), false);
        PINK_EMP = register("suso:pink_emp", new EMPBlock(AbstractBlock.Settings.create().strength(-1.0F, 3600000.0F).dropsNothing().nonOpaque().noCollision()), false);
        PURPLE_EMP = register("suso:purple_emp", new EMPBlock(AbstractBlock.Settings.create().strength(-1.0F, 3600000.0F).dropsNothing().nonOpaque().noCollision()), false);
        BLUE_EMP = register("suso:blue_emp", new EMPBlock(AbstractBlock.Settings.create().strength(-1.0F, 3600000.0F).dropsNothing().nonOpaque().noCollision()), false);
        CYAN_EMP = register("suso:cyan_emp", new EMPBlock(AbstractBlock.Settings.create().strength(-1.0F, 3600000.0F).dropsNothing().nonOpaque().noCollision()), false);
        LIGHT_BLUE_EMP = register("suso:light_blue_emp", new EMPBlock(AbstractBlock.Settings.create().strength(-1.0F, 3600000.0F).dropsNothing().nonOpaque().noCollision()), false);
        GREEN_EMP = register("suso:green_emp", new EMPBlock(AbstractBlock.Settings.create().strength(-1.0F, 3600000.0F).dropsNothing().nonOpaque().noCollision()), false);
        LIME_EMP = register("suso:lime_emp", new EMPBlock(AbstractBlock.Settings.create().strength(-1.0F, 3600000.0F).dropsNothing().nonOpaque().noCollision()), false);
        YELLOW_EMP = register("suso:yellow_emp", new EMPBlock(AbstractBlock.Settings.create().strength(-1.0F, 3600000.0F).dropsNothing().nonOpaque().noCollision()), false);
        ORANGE_EMP = register("suso:orange_emp", new EMPBlock(AbstractBlock.Settings.create().strength(-1.0F, 3600000.0F).dropsNothing().nonOpaque().noCollision()), false);
        RED_EMP = register("suso:red_emp", new EMPBlock(AbstractBlock.Settings.create().strength(-1.0F, 3600000.0F).dropsNothing().nonOpaque().noCollision()), false);

        GRAY_GUNK = register("suso:gray_gunk", new Block(AbstractBlock.Settings.create().mapColor(MapColor.GRAY).slipperiness(0.99F).sounds(BlockSoundGroup.SLIME)), false);
        WHITE_GUNK = register("suso:white_gunk", new Block(AbstractBlock.Settings.create().mapColor(MapColor.WHITE).slipperiness(0.99F).sounds(BlockSoundGroup.SLIME)), false);
        PINK_GUNK = register("suso:pink_gunk", new Block(AbstractBlock.Settings.create().mapColor(MapColor.PINK).slipperiness(0.99F).sounds(BlockSoundGroup.SLIME)), false);
        PURPLE_GUNK = register("suso:purple_gunk", new Block(AbstractBlock.Settings.create().mapColor(MapColor.PURPLE).slipperiness(0.99F).sounds(BlockSoundGroup.SLIME)), false);
        BLUE_GUNK = register("suso:blue_gunk", new Block(AbstractBlock.Settings.create().mapColor(MapColor.BLUE).slipperiness(0.99F).sounds(BlockSoundGroup.SLIME)), false);
        CYAN_GUNK = register("suso:cyan_gunk", new Block(AbstractBlock.Settings.create().mapColor(MapColor.CYAN).slipperiness(0.99F).sounds(BlockSoundGroup.SLIME)), false);
        LIGHT_BLUE_GUNK = register("suso:light_blue_gunk", new Block(AbstractBlock.Settings.create().mapColor(MapColor.LIGHT_BLUE).slipperiness(0.99F).sounds(BlockSoundGroup.SLIME)), false);
        GREEN_GUNK = register("suso:green_gunk", new Block(AbstractBlock.Settings.create().mapColor(MapColor.GREEN).slipperiness(0.99F).sounds(BlockSoundGroup.SLIME)), false);
        LIME_GUNK = register("suso:lime_gunk", new Block(AbstractBlock.Settings.create().mapColor(MapColor.LIME).slipperiness(0.99F).sounds(BlockSoundGroup.SLIME)), false);
        YELLOW_GUNK = register("suso:yellow_gunk", new Block(AbstractBlock.Settings.create().mapColor(MapColor.YELLOW).slipperiness(0.99F).sounds(BlockSoundGroup.SLIME)), false);
        ORANGE_GUNK = register("suso:orange_gunk", new Block(AbstractBlock.Settings.create().mapColor(MapColor.ORANGE).slipperiness(0.99F).sounds(BlockSoundGroup.SLIME)), false);
        RED_GUNK = register("suso:red_gunk", new Block(AbstractBlock.Settings.create().mapColor(MapColor.RED).slipperiness(0.99F).sounds(BlockSoundGroup.SLIME)), false);

        GUNK_ENTITY = register("suso:gunk_entity", BlockEntityType.Builder.create(GunkBlockEntity::new, GRAY_GUNK, WHITE_GUNK, PINK_GUNK, PURPLE_GUNK, BLUE_GUNK, CYAN_GUNK, LIGHT_BLUE_GUNK, GREEN_GUNK, LIME_GUNK, YELLOW_GUNK, ORANGE_GUNK, RED_GUNK).build());

        PRIMATICA_POWERUP = register("suso:primatica_powerup", new PrimaticaPowerupBlock(AbstractBlock.Settings.create().strength(-1.0F, 3600000.0F).dropsNothing().nonOpaque().noCollision()), false);
        PRIMATICA_POWERUP_ENTITY = register("suso:primatica_powerup_entity", BlockEntityType.Builder.create(PrimaticaPowerupBlockEntity::new, PRIMATICA_POWERUP).build());
        BlockEntityRendererFactories.register(PRIMATICA_POWERUP_ENTITY, ctx -> new PrimaticaPowerupRenderer());
        PRIMATICA_RESPAWN = register("suso:primatica_respawn", new PrimaticaRespawnBlock(AbstractBlock.Settings.create().strength(-1.0F, 3600000.0F).dropsNothing().nonOpaque().noCollision()), false);
        PRIMATICA_RESPAWN_ENTITY = register("suso:primatica_respawn_entity", BlockEntityType.Builder.create(PrimaticaRespawnBlockEntity::new, PRIMATICA_RESPAWN).build());
        BlockEntityRendererFactories.register(PRIMATICA_RESPAWN_ENTITY, ctx -> new PrimaticaRespawnRenderer());
        PRIMATICA_DOOR = register("suso:primatica_door", new PrimaticaDoorBlock(AbstractBlock.Settings.create().strength(-1.0F, 3600000.0F).dropsNothing().nonOpaque().noCollision()), false);
        PRIMATICA_DOOR_ENTITY = register("suso:primatica_door_entity", BlockEntityType.Builder.create(PrimaticaDoorBlockEntity::new, PRIMATICA_DOOR).build());
        BlockEntityRendererFactories.register(PRIMATICA_DOOR_ENTITY, ctx -> new PrimaticaDoorRenderer());
    }
}
