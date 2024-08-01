package suso.event_base.custom.entities;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.loading.math.MathParser;
import software.bernie.geckolib.model.GeoModel;

public class PrimaticaOrbModel extends GeoModel<PrimaticaOrbEntity> {
    @Override
    public Identifier getModelResource(PrimaticaOrbEntity object) {
        return Identifier.of("suso", "geo/primatica_orb.geo.json");
    }

    @Override
    public Identifier getTextureResource(PrimaticaOrbEntity object) {
        return Identifier.of("suso", "textures/entity/primatica_orb.png");
    }

    @Override
    public Identifier getAnimationResource(PrimaticaOrbEntity animatable) {
        return Identifier.of("suso", "animations/entity/primatica_orb.animation.json");
    }

    @Override
    public void applyMolangQueries(AnimationState<PrimaticaOrbEntity> animationState, double animTime) {
        super.applyMolangQueries(animationState, animTime);

        MinecraftClient client = MinecraftClient.getInstance();
        PrimaticaOrbEntity entity = animationState.getAnimatable();

        MathParser.setVariable("query.face_camera_x", () -> {
            Vec3d cameraPos = client.gameRenderer.getCamera().getPos();
            Vec3d offset = cameraPos.subtract(entity.getPos());
            return Math.toDegrees(Math.atan2(offset.z, offset.x)) - 90.0;
        });

        MathParser.setVariable("query.face_camera_y", () -> {
            Vec3d cameraPos = client.gameRenderer.getCamera().getPos();
            Vec3d offset = cameraPos.subtract(entity.getPos());
            return -Math.toDegrees(Math.atan2(offset.y, offset.horizontalLength()));
        });
    }
}
