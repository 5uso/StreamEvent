package suso.event_base.custom.blocks.entity;

import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class PrimaticaDoorModel extends AnimatedGeoModel<PrimaticaDoorBlockEntity> {
    @Override
    public Identifier getModelResource(PrimaticaDoorBlockEntity object) {
        return Identifier.of("suso", object.isDiagonal() ? "geo/primatica_door_diag.geo.json" : "geo/primatica_door.geo.json");
    }

    @Override
    public Identifier getTextureResource(PrimaticaDoorBlockEntity object) {
        return Identifier.of("suso", "textures/block/primatica_door.png");
    }

    @Override
    public Identifier getAnimationResource(PrimaticaDoorBlockEntity animatable) {
        return Identifier.of("suso", animatable.isDiagonal() ? "animations/block/primatica_door_diag.animation.json" : "animations/block/primatica_door.animation.json");
    }
}
