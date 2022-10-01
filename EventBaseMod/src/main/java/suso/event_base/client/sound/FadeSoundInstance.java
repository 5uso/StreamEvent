package suso.event_base.client.sound;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.sound.MovingSoundInstance;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;

@Environment(EnvType.CLIENT)
public class FadeSoundInstance extends MovingSoundInstance {
    private final FadeInfo volumeFade, pitchFade;

    public FadeSoundInstance(Identifier id, float startVolume, float startPitch, boolean loop) {
        super(new SoundEvent(id), SoundCategory.RECORDS, Random.create(2711));

        this.attenuationType = AttenuationType.NONE;
        this.relative = true;
        this.volume = startVolume;
        this.pitch = startPitch;
        this.repeat = loop;
        this.repeatDelay = 0;

        this.volumeFade = new FadeInfo(startVolume);
        this.pitchFade = new FadeInfo(startPitch);
    }

    @Override
    public void tick() {
        this.volume = volumeFade.fade();
        this.pitch = pitchFade.fade();
    }

    @Override
    public boolean shouldAlwaysPlay() {
        return true;
    }

    public void setVolumeFade(float target, int fadeLengthTicks) {
        this.volumeFade.setFade(this.volume, target, fadeLengthTicks);
    }

    public void setPitchFade(float target, int fadeLengthTicks) {
        this.pitchFade.setFade(this.pitch, target, fadeLengthTicks);
    }

    private static class FadeInfo {
        private float from, to;
        private int length, counter;

        public FadeInfo(float startValue) {
            this.from = startValue;
            this.to = startValue;
            this.length = 0;
            this.counter = 0;
        }

        public void setFade(float current, float target, int fadeLengthTicks) {
            from = current;
            to = target;

            length = fadeLengthTicks;
            counter = 0;
        }

        public float fade() {
            if(counter < length) {
                double t = counter / (double) length;
                double progress = 1.0 - Math.exp(-t * 4.0);
                counter++;

                return (float) (from + progress * (to - from));
            }

            return to;
        }
    }
}
