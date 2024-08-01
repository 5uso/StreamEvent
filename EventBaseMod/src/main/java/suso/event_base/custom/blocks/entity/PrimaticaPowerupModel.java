package suso.event_base.custom.blocks.entity;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.molang.MolangParser;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.resource.GeckoLibCache;

public class PrimaticaPowerupModel extends AnimatedGeoModel<PrimaticaPowerupBlockEntity> {
    @Override
    public Identifier getModelResource(PrimaticaPowerupBlockEntity object) {
        return Identifier.of("suso", "geo/primatica_powerup.geo.json");
    }

    @Override
    public Identifier getTextureResource(PrimaticaPowerupBlockEntity object) {
        String id = switch (object.type) {
            case AGILITY -> "textures/block/primatica_powerup/agility.png";
            case EMP -> "textures/block/primatica_powerup/emp.png";
            case GUNK -> "textures/block/primatica_powerup/gunk.png";
            case ARROW -> "textures/block/primatica_powerup/arrow.png";
            case BRIDGE -> "textures/block/primatica_powerup/bridge.png";
            case GRAVITY -> "textures/block/primatica_powerup/gravity.png";
        };

        return Identifier.of("suso", id);
    }

    @Override
    public Identifier getAnimationResource(PrimaticaPowerupBlockEntity animatable) {
        return Identifier.of("suso", "animations/block/primatica_powerup.animation.json");
    }

    @Override
    public void setMolangQueries(IAnimatable animatable, double seekTime) {
        super.setMolangQueries(animatable, seekTime);

        MolangParser parser = GeckoLibCache.getInstance().parser;
        MinecraftClient client = MinecraftClient.getInstance();

        if(animatable instanceof BlockEntity blockEntity) {
            parser.setValue("query.face_camera_x", () -> {
                Vec3d cameraPos = client.gameRenderer.getCamera().getPos();
                BlockPos blockPos = blockEntity.getPos();
                Vec3d offset = cameraPos.subtract(blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5);
                return Math.toDegrees(Math.atan2(offset.z, offset.x)) + 90.0;
            });

            parser.setValue("query.face_camera_y", () -> {
                Vec3d cameraPos = client.gameRenderer.getCamera().getPos();
                BlockPos blockPos = blockEntity.getPos();
                Vec3d offset = cameraPos.subtract(blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5);
                return -Math.toDegrees(Math.atan2(offset.y, offset.horizontalLength()));
            });
        }
    }
}
