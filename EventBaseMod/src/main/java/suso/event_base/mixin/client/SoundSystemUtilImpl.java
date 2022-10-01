package suso.event_base.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.sound.SoundSystem;
import net.minecraft.client.sound.TickableSoundInstance;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import suso.event_base.client.sound.FadeSoundInstance;
import suso.event_base.client.sound.ISoundSystemUtil;

import java.util.List;

@Mixin(SoundSystem.class) @Environment(EnvType.CLIENT)
public class SoundSystemUtilImpl implements ISoundSystemUtil {

    @Shadow @Final private List<TickableSoundInstance> tickingSounds;

    @Override
    public void sendFadeVolume(Identifier id, float target, int fadeLengthTicks) {
        for(TickableSoundInstance sound : this.tickingSounds) {
            if(sound instanceof FadeSoundInstance && sound.getId().equals(id)) {
                ((FadeSoundInstance) sound).setVolumeFade(target, fadeLengthTicks);
            }
        }
    }

    @Override
    public void sendFadePitch(Identifier id, float target, int fadeLengthTicks) {
        for(TickableSoundInstance sound : this.tickingSounds) {
            if(sound instanceof FadeSoundInstance && sound.getId().equals(id)) {
                ((FadeSoundInstance) sound).setPitchFade(target, fadeLengthTicks);
            }
        }
    }
}
