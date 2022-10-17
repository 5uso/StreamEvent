package suso.event_manage.injected_interfaces;

import net.minecraft.util.math.Vec3d;

public interface ServerPlayerEntityExtended {
    Vec3d getPosDelta();
    void setJumpPressed(boolean isJumpPressed);
    boolean isJumpPressed();
}
