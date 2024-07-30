package suso.event_manage.injected_interfaces;

import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;

public interface ParticleEffectMirror extends ParticleEffect {
    @Override
    default ParticleType<?> getType() {
        return null;
    }
}
