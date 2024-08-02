package suso.event_base.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    @Inject(
            method = "getOffGroundSpeed",
            at = @At("RETURN"),
            cancellable = true
    )
    private void agilityIncreaseAirStrafe(CallbackInfoReturnable<Float> cir) {
        StatusEffectInstance e = ((LivingEntity)(Object) this).getStatusEffect(StatusEffects.JUMP_BOOST);
        if(e != null && e.getAmplifier() == 4) cir.setReturnValue(cir.getReturnValue() * 1.5F);
    }
}
