public class StopWatch {

    // Assuming these constants are defined elsewhere in the class:
    private static final int STATE_RUNNING = 1; // Represents the stopwatch is running
    private static final int STATE_SUSPENDED = 2; // Represents the stopwatch is suspended
    private static final int STATE_STOPPED = 3; // Represents the stopwatch has been stopped

    private long startTime;
    private long stopTime;
    private int runningState;

    public void start() {
        if (this.runningState == STATE_RUNNING) {
            throw new IllegalStateException("Stopwatch is already running.");
        }
        this.startTime = System.currentTimeMillis();
        this.runningState = STATE_RUNNING;
    }

    public void suspend() {
        if (this.runningState != STATE_RUNNING) {
            throw new IllegalStateException("Stopwatch is not running and cannot be suspended.");
        }
        stopTime = System.currentTimeMillis() - startTime;
        this.runningState = STATE_SUSPENDED;
    }

    public void stop() {
        if (this.runningState == STATE_SUSPENDED) {
            throw new IllegalStateException("Stopwatch is suspended. Use resume before stopping.");
        }
        if (this.runningState != STATE_RUNNING && this.runningState != STATE_STOPPED) {
            throw new IllegalStateException("Stopwatch is not in a valid state to stop.");
        }
        if (this.runningState == STATE_STOPPED) {
            return; // Do nothing since it's already stopped
        }

        this.stopTime = System.currentTimeMillis();
        this.runningState = STATE_STOPPED;
    }

    public long getTime() {
        switch (this.runningState) {
            case STATE_RUNNING:
                return System.currentTimeMillis() - startTime;
            case STATE_SUSPENDED:
                return stopTime + (System.currentTimeMillis() - startTime);
            case STATE_STOPPED:
                return stopTime;
            default:
                throw new IllegalStateException("Stopwatch is in an unknown state.");
        }
    }

    // Other methods and class content...
}