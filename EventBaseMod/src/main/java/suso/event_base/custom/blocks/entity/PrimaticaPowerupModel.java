package suso.event_base.custom.blocks.entity;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.loading.math.MathParser;
import software.bernie.geckolib.model.GeoModel;
import suso.event_common.custom.blocks.entity.PrimaticaPowerupBlockEntity;

public class PrimaticaPowerupModel extends GeoModel<PrimaticaPowerupBlockEntityClient> {
    @Override
    public Identifier getModelResource(PrimaticaPowerupBlockEntityClient object) {
        return Identifier.of("suso", "geo/primatica_powerup.geo.json");
    }

    @Override
    public Identifier getTextureResource(PrimaticaPowerupBlockEntityClient object) {
        String id = switch (object.getPowerupType()) {
            case PrimaticaPowerupBlockEntity.Powerups.AGILITY -> "textures/block/primatica_powerup/agility.png";
            case PrimaticaPowerupBlockEntity.Powerups.EMP -> "textures/block/primatica_powerup/emp.png";
            case PrimaticaPowerupBlockEntity.Powerups.GUNK -> "textures/block/primatica_powerup/gunk.png";
            case PrimaticaPowerupBlockEntity.Powerups.ARROW -> "textures/block/primatica_powerup/arrow.png";
            case PrimaticaPowerupBlockEntity.Powerups.BRIDGE -> "textures/block/primatica_powerup/bridge.png";
            case PrimaticaPowerupBlockEntity.Powerups.GRAVITY -> "textures/block/primatica_powerup/gravity.png";
        };

        return Identifier.of("suso", id);
    }

    @Override
    public Identifier getAnimationResource(PrimaticaPowerupBlockEntityClient animatable) {
        return Identifier.of("suso", "animations/block/primatica_powerup.animation.json");
    }

    @Override
    public void applyMolangQueries(AnimationState<PrimaticaPowerupBlockEntityClient> animationState, double animTime) {
        super.applyMolangQueries(animationState, animTime);

        MinecraftClient client = MinecraftClient.getInstance();
        PrimaticaPowerupBlockEntityClient blockEntity = animationState.getAnimatable();

        MathParser.setVariable("query.face_camera_x", () -> {
            Vec3d cameraPos = client.gameRenderer.getCamera().getPos();
            BlockPos blockPos = blockEntity.getPos();
            Vec3d offset = cameraPos.subtract(blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5);
            return Math.toDegrees(Math.atan2(offset.z, offset.x)) + 90.0;
        });

        MathParser.setVariable("query.face_camera_y", () -> {
            Vec3d cameraPos = client.gameRenderer.getCamera().getPos();
            BlockPos blockPos = blockEntity.getPos();
            Vec3d offset = cameraPos.subtract(blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5);
            return -Math.toDegrees(Math.atan2(offset.y, offset.horizontalLength()));
        });
    }
}
