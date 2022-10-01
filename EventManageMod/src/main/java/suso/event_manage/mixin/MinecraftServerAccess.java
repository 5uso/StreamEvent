package suso.event_manage.mixin;

import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MinecraftServer.class)
public interface MinecraftServerAccess {
    @Accessor static Logger getLOGGER() {
        throw new AssertionError();
    }
}
