package suso.event_base.custom.entities;

import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.entity.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import suso.event_common.custom.entities.PrimaticaOrbEntity;

public class CustomEntities {
    private static <T extends Entity> EntityType<T> register(String id, EntityType<T> entity) {
        return Registry.register(Registries.ENTITY_TYPE, id, entity);
    }

    public static void register() {
        PrimaticaOrbEntityClient.TYPE = register("suso:primatica_orb", PrimaticaOrbEntity.TYPE_SETTINGS.build(PrimaticaOrbEntityClient::new));
        EntityRendererRegistry.register(PrimaticaOrbEntityClient.TYPE, PrimaticaOrbRenderer::new);
    }
}
