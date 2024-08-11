package suso.event_manage.custom.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import suso.event_common.custom.entities.PrimaticaOrbEntity;

public class CustomEntities {
    private static <T extends Entity> EntityType<T> register(String id, EntityType<T> entity) {
        return Registry.register(Registries.ENTITY_TYPE, id, entity);
    }

    public static void register() {
        PrimaticaOrbEntity.TYPE = register("suso:primatica_orb", PrimaticaOrbEntity.TYPE_SETTINGS.build(PrimaticaOrbEntity::new));
    }
}
