package suso.event_base.client.shader;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.SimpleFramebuffer;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Environment(EnvType.CLIENT)
public class CustomUniformStore {
    private static final Map<String, int[]> intUniforms = new HashMap<>();
    private static final Map<String, float[]> floatUniforms = new HashMap<>();

    public static boolean overridingPost = false;
    private static String postOverride = "";

    public static final Framebuffer aux = new SimpleFramebuffer(MinecraftClient.getInstance().getWindow().getFramebufferWidth(), MinecraftClient.getInstance().getWindow().getFramebufferHeight(), true, false);
    public static int readID = 0, drawID = 0, globID = 0;

    public static void setUniform(String name, float[] value) {
        floatUniforms.put(name, value);
    }

    public static void setUniform(String name, int[] value) {
        intUniforms.put(name, value);
    }

    public static Set<Map.Entry<String, float[]>> floatEntries() {
        return floatUniforms.entrySet();
    }

    public static Set<Map.Entry<String, int[]>> intEntries() {
        return intUniforms.entrySet();
    }

    public static void setPostOverride(String name) {
        postOverride = name;
        overridingPost = !name.equals("minecraft:none");
    }

    public static String getPostOverride() {
        return postOverride;
    }
}
