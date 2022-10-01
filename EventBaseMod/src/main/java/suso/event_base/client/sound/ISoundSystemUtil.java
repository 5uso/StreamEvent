package suso.event_base.client.sound;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public interface ISoundSystemUtil {
    void sendFadeVolume(Identifier id, float target, int fadeLengthTicks);
    void sendFadePitch(Identifier id, float target, int fadeLengthTicks);
}
