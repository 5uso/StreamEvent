package suso.event_manage.state_handlers;

import org.jetbrains.annotations.Nullable;

import java.util.Queue;

public interface TickableInstance {
    boolean tick();
    void remove();

    default boolean ifTickRemove() {
        if(tick()) {
            remove();
            return true;
        }
        return false;
    }
}
