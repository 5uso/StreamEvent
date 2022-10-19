package suso.event_manage.injected_interfaces;

import net.minecraft.util.math.Vec3d;

public interface ServerPlayerEntityExtended {
    default Vec3d getPosDelta() {
        return null;
    }

    default void setJumpPressed(boolean isJumpPressed) {
    }

    default boolean isJumpPressed() {
        return false;
    }
}
