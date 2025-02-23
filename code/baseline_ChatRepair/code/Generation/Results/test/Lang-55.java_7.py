public class Stopwatch {
    private long startTime;
    private long stopTime;
    private long totalTime;
    private int runningState;

    public static final int STATE_STOPPED = 0;
    public static final int STATE_RUNNING = 1;
    public static final int STATE_SUSP