package suso.event_base;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import suso.event_base.client.debug.DebugCommands;
import suso.event_base.custom.blocks.CustomBlocks;
import suso.event_base.custom.entities.CustomEntities;
import suso.event_base.custom.items.CustomItems;
import suso.event_common.EventConstants;

public class Event_base implements ModInitializer {
    @Override
    public void onInitialize() {
        if(EventConstants.DEBUG) {
            CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> DebugCommands.register(dispatcher));
        }

        CustomBlocks.register();
        CustomItems.register();
        CustomEntities.register();
    }
}
