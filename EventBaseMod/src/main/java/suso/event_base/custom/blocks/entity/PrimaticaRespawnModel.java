package suso.event_base.custom.blocks.entity;

import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class PrimaticaRespawnModel extends GeoModel<PrimaticaRespawnBlockEntityClient> {
    @Override
    public Identifier getModelResource(PrimaticaRespawnBlockEntityClient object) {
        return Identifier.of("suso", "geo/primatica_respawn.geo.json");
    }

    @Override
    public Identifier getTextureResource(PrimaticaRespawnBlockEntityClient object) {
        return Identifier.of("suso", "textures/block/primatica_respawn.png");
    }

    @Override
    public Identifier getAnimationResource(PrimaticaRespawnBlockEntityClient animatable) {
        return Identifier.of("suso", "animations/block/primatica_respawn.animation.json");
    }
}
