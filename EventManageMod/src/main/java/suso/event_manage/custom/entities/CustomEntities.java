package suso.event_manage.custom.entities;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.world.World;

public class CustomEntities {
    public static EntityType<PrimaticaOrbEntity> PRIMATICA_ORB;

    private static <T extends Entity> EntityType<T> register(String id, EntityType<T> entity) {
        return Registry.register(Registries.ENTITY_TYPE, id, entity);
    }

    public static void register() {
        PRIMATICA_ORB = register("suso:primatica_orb", FabricEntityTypeBuilder.create(SpawnGroup.MISC, (EntityType<PrimaticaOrbEntity> type, World world) -> new PrimaticaOrbEntity(type, world)).dimensions(EntityDimensions.fixed(1.0f, 1.0f)).disableSaving().build());
    }
}
