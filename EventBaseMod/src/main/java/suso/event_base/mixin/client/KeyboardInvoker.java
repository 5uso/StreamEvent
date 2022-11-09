package suso.event_base.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Keyboard.class) @Environment(EnvType.CLIENT)
public interface KeyboardInvoker {
    @Invoker
    void invokeDebugLog(String key, Object ... args);

    @Invoker
    void invokeDebugError(String key, Object ... args);
}
