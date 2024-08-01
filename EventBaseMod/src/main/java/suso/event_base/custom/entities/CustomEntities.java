package suso.event_base.custom.entities;

import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class CustomEntities {
    public static EntityType<PrimaticaOrbEntity> PRIMATICA_ORB;

    private static <T extends Entity> EntityType<T> register(String id, EntityType<T> entity) {
        return Registry.register(Registries.ENTITY_TYPE, id, entity);
    }

    public static void register() {
        PRIMATICA_ORB = register("suso:primatica_orb", EntityType.Builder.create(PrimaticaOrbEntity::new, SpawnGroup.MISC).dimensions(1.0f, 1.0f).disableSaving().build());
        EntityRendererRegistry.register(PRIMATICA_ORB, PrimaticaOrbRenderer::new);
        FabricDefaultAttributeRegistry.register(CustomEntities.PRIMATICA_ORB, LivingEntity.createLivingAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 10000.0));
    }
}
