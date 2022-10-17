package suso.event_base.mixin.client;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    @ModifyConstant(
            method = "tickMovement",
            constant = @Constant(
                    floatValue = 0.02F
            )
    )
    private float agilityIncreaseAirStrafe(float constant) {
        StatusEffectInstance e = ((LivingEntity)(Object) this).getStatusEffect(StatusEffects.JUMP_BOOST);
        if(e != null && e.getAmplifier() == 4) return constant * 1.5F;
        return constant;
    }
}
