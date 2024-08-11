package suso.event_manage.custom.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.world.World;
import suso.event_common.custom.entities.PrimaticaOrbEntity;

public class CustomEntities {
    private static <T extends Entity> EntityType<T> register(String id, EntityType<T> entity) {
        return Registry.register(Registries.ENTITY_TYPE, id, entity);
    }

    public static void register() {
        PrimaticaOrbEntity.TYPE = register("suso:primatica_orb", EntityType.Builder.create((EntityType<PrimaticaOrbEntity> type, World world) -> new PrimaticaOrbEntity(type, world), SpawnGroup.MISC).dimensions(1.0f, 1.0f).disableSaving().build());
    }
}
