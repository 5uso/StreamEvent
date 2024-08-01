package suso.event_base.custom.blocks.entity;

import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class PrimaticaRespawnModel extends AnimatedGeoModel<PrimaticaRespawnBlockEntity> {
    @Override
    public Identifier getModelResource(PrimaticaRespawnBlockEntity object) {
        return Identifier.of("suso", "geo/primatica_respawn.geo.json");
    }

    @Override
    public Identifier getTextureResource(PrimaticaRespawnBlockEntity object) {
        return Identifier.of("suso", "textures/block/primatica_respawn.png");
    }

    @Override
    public Identifier getAnimationResource(PrimaticaRespawnBlockEntity animatable) {
        return Identifier.of("suso", "animations/block/primatica_respawn.animation.json");
    }
}
