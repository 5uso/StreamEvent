package suso.event_manage.state_handlers;

public class ScheduleInstance implements TickableInstance {
    private int ticksLeft;
    private final Scheduled function;

    public ScheduleInstance(int ticks, Scheduled function) {
        this.ticksLeft = ticks;
        this.function = function;
    }

    @Override
    public boolean tick() {
        if(--ticksLeft < 0) {
            function.execute();
            return true;
        }

        return false;
    }

    @Override
    public void remove() {

    }

    @FunctionalInterface
    public interface Scheduled {
        void execute();
    }
}
