package suso.event_common.custom.blocks;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.MapColor;
import net.minecraft.sound.BlockSoundGroup;

public class OtherBlockSettings {
    public static AbstractBlock.Settings gunk(MapColor mapColor) {
        return AbstractBlock.Settings.create().slipperiness(0.99F).sounds(BlockSoundGroup.SLIME).mapColor(mapColor);
    }

    public static AbstractBlock.Settings holoblock(MapColor mapColor) {
        return AbstractBlock.Settings.create().sounds(BlockSoundGroup.AMETHYST_BLOCK).nonOpaque().mapColor(mapColor);
    }
}
