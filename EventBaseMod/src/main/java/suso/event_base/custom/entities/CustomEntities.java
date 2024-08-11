package suso.event_base.custom.entities;

import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.entity.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class CustomEntities {
    private static <T extends Entity> EntityType<T> register(String id, EntityType<T> entity) {
        return Registry.register(Registries.ENTITY_TYPE, id, entity);
    }

    public static void register() {
        PrimaticaOrbEntityClient.TYPE = register("suso:primatica_orb", EntityType.Builder.create(PrimaticaOrbEntityClient::new, SpawnGroup.MISC).dimensions(1.0f, 1.0f).disableSaving().build());
        EntityRendererRegistry.register(PrimaticaOrbEntityClient.TYPE, PrimaticaOrbRenderer::new);
    }
}
