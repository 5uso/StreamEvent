package suso.event_base.client.shader;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface IGlUniformUtil {
    void set(int[] values);
}
