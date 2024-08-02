package suso.event_base.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Keyboard.class) @Environment(EnvType.CLIENT)
public class KeyboardMixin {
    @Redirect(
            method = "onKey",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/GameRenderer;togglePostProcessorEnabled()V"
            )
    )
    private void handleF4(GameRenderer instance) {
        if(InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), GLFW.GLFW_KEY_F3)) return;

        instance.togglePostProcessorEnabled();
    }
}
