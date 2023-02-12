package suso.event_base.custom.blocks.entity;

import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class PrimaticaDoorModel extends AnimatedGeoModel<PrimaticaDoorBlockEntity> {
    @Override
    public Identifier getModelResource(PrimaticaDoorBlockEntity object) {
        return new Identifier(object.isDiagonal() ? "suso:geo/primatica_door_diag.geo.json" : "suso:geo/primatica_door.geo.json");
    }

    @Override
    public Identifier getTextureResource(PrimaticaDoorBlockEntity object) {
        return new Identifier("suso:textures/block/primatica_door.png");
    }

    @Override
    public Identifier getAnimationResource(PrimaticaDoorBlockEntity animatable) {
        return new Identifier(animatable.isDiagonal() ? "suso:animations/block/primatica_door_diag.animation.json" : "suso:animations/block/primatica_door.animation.json");
    }
}
