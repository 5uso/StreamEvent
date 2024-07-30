package suso.event_manage.mixin;

import net.minecraft.particle.ParticleType;
import org.spongepowered.asm.mixin.Mixin;
import suso.event_manage.injected_interfaces.ParticleEffectMirror;

@Mixin(ParticleType.class)
public abstract class ParticleTypeMixin implements ParticleEffectMirror {
    @Override
    public ParticleType<?> getType() {
        return (ParticleType<?>)(Object) this;
    }
}
