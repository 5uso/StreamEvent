package suso.event_base.custom.entities;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.molang.MolangParser;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.resource.GeckoLibCache;

public class PrimaticaOrbModel extends AnimatedGeoModel<PrimaticaOrbEntity> {
    @Override
    public Identifier getModelResource(PrimaticaOrbEntity object) {
        return new Identifier("suso:geo/primatica_orb.geo.json");
    }

    @Override
    public Identifier getTextureResource(PrimaticaOrbEntity object) {
        return new Identifier("suso:textures/entity/primatica_orb.png");
    }

    @Override
    public Identifier getAnimationResource(PrimaticaOrbEntity animatable) {
        return new Identifier("suso:animations/entity/primatica_orb.animation.json");
    }

    @Override
    public void setMolangQueries(IAnimatable animatable, double seekTime) {
        super.setMolangQueries(animatable, seekTime);

        MolangParser parser = GeckoLibCache.getInstance().parser;
        MinecraftClient client = MinecraftClient.getInstance();

        if(animatable instanceof Entity entity) {
            parser.setValue("query.face_camera_x", () -> {
                Vec3d cameraPos = client.gameRenderer.getCamera().getPos();
                Vec3d offset = cameraPos.subtract(entity.getPos());
                return Math.toDegrees(Math.atan2(offset.z, offset.x)) - 90.0;
            });

            parser.setValue("query.face_camera_y", () -> {
                Vec3d cameraPos = client.gameRenderer.getCamera().getPos();
                Vec3d offset = cameraPos.subtract(entity.getPos());
                return -Math.toDegrees(Math.atan2(offset.y, offset.horizontalLength()));
            });
        }
    }
}
