package suso.event_base.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundListener;
import net.minecraft.client.sound.SoundSystem;
import net.minecraft.sound.SoundCategory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SoundSystem.class) @Environment(EnvType.CLIENT)
public abstract class SoundSystemMixin {
    @Shadow protected abstract float getAdjustedVolume(float volume, SoundCategory category);

    @Inject(
            method = "getAdjustedVolume(Lnet/minecraft/client/sound/SoundInstance;)F",
            at = @At("HEAD"),
            cancellable = true
    )
    private void adjustAlwaysPLayVolume(SoundInstance sound, CallbackInfoReturnable<Float> cir) {
        if(sound.shouldAlwaysPlay()) {
            cir.setReturnValue(0.001F + this.getAdjustedVolume(sound.getVolume(), sound.getCategory()));
            cir.cancel();
        }
    }

    @Redirect(
            method = "tick()V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/option/GameOptions;getSoundVolume(Lnet/minecraft/sound/SoundCategory;)F"
            )
    )
    private float hijackCategoryVolume(GameOptions instance, SoundCategory category) {
        return 1.0F;
    }

    @Redirect(
            method = "play(Lnet/minecraft/client/sound/SoundInstance;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/sound/SoundListener;getVolume()F"
            )
    )
    private float hijackMasterVolume(SoundListener instance) {
        return 1.0F;
    }
}
