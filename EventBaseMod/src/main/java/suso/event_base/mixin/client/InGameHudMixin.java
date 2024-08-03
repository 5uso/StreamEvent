package suso.event_base.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class) @Environment(EnvType.CLIENT)
public class InGameHudMixin {
    @Inject(
            method = "renderChat",
            at = @At(value = "HEAD")
    )
    private void offsetChat(DrawContext ctx, RenderTickCounter tickCounter, CallbackInfo ci) {
        ctx.getMatrices().push();
        ctx.getMatrices().translate(0.0, 48 - ctx.getScaledWindowHeight() / 2.0f, 0.0);
    }

    @Inject(
            method = "renderChat",
            at = @At(value = "TAIL")
    )
    private void endOffsetChat(DrawContext ctx, RenderTickCounter tickCounter, CallbackInfo ci) {
        ctx.getMatrices().pop();
    }
}
