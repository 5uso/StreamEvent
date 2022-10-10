package suso.event_base.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.client.sound.SoundSystem;
import net.minecraft.sound.SoundCategory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SoundManager.class) @Environment(EnvType.CLIENT)
public class SoundManagerMixin {
    @Shadow @Final private SoundSystem soundSystem;

    @Inject(
            method = "updateSoundVolume",
            at = @At("HEAD"),
            cancellable = true
    )
    private void updateSoundsDontStop(SoundCategory category, float volume, CallbackInfo ci) {
        if(volume <= 0) {
            this.soundSystem.updateSoundVolume(category, volume);
            ci.cancel();
        }
    }
}
