package suso.event_manage.state_handlers;

import net.minecraft.server.network.ServerPlayerEntity;

public class PlayerScheduleInstance implements TickableInstance {
    private int ticksLeft;
    private final ServerPlayerEntity player;
    private final PlayerScheduled function;

    public PlayerScheduleInstance(ServerPlayerEntity player, int ticks, PlayerScheduled function) {
        this.ticksLeft = ticks;
        this.player = player;
        this.function = function;
    }

    @Override
    public boolean tick() {
        if(--ticksLeft < 0) {
            function.execute(player);
            return true;
        }

        return false;
    }

    @Override
    public void remove() {

    }

    @FunctionalInterface
    public interface PlayerScheduled {
        void execute(ServerPlayerEntity player);
    }
}
