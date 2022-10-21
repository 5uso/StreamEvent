package suso.event_base.mixin.client.fabric;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.impl.client.indigo.renderer.render.BlockRenderInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(BlockRenderInfo.class) @Environment(EnvType.CLIENT)
public class BlockRenderInfoMixin {
    @ModifyConstant(
            method = "blockColor",
            constant = @Constant(intValue = 0xFF000000),
            remap = false
    )
    private int removeAlphaMask(int mask) {
        return 0;
    }
}
