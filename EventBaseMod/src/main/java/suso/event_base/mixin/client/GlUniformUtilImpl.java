package suso.event_base.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gl.GlUniform;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import suso.event_base.client.shader.IGlUniformUtil;

import java.nio.IntBuffer;

@Mixin(GlUniform.class) @Environment(EnvType.CLIENT)
public abstract class GlUniformUtilImpl implements IGlUniformUtil {
    @Shadow @Final private int count;

    @Shadow @Final private static Logger LOGGER;

    @Shadow @Final private IntBuffer intData;

    @Shadow protected abstract void markStateDirty();

    @Override
    public void set(int[] values) {
        if (values.length < this.count) {
            LOGGER.warn("Uniform.set called with a too-small value array (expected {}, got {}). Ignoring.", this.count, values.length);
        } else {
            this.intData.position(0);
            this.intData.put(values);
            this.intData.position(0);
            this.markStateDirty();
        }
    }
}
