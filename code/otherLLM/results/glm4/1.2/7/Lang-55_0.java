public void stop() {
    if (this.runningState != STATE_RUNNING && this.runningState != STATE_SUSPENDED) {
        throw new IllegalStateException("Stopwatch is not running or already stopped.");
    }
    
    // If the stopwatch is running, we want to capture the current time,
    // but if it's suspended, do not change the elapsed time.
    if (this.runningState == STATE_RUNNING) {
        stopTime = System.currentTimeMillis();
    }
    
    this.runningState = STATE_STOPPED;
}

// Assuming there are constants or fields that define the states
public static final int STATE_RUNNING = 1;
public static final int STATE_SUSPENDED = 2;
public static final int STATE_STOPPED = 3;

// An example of a method to get the time, which should not increment when suspended:
public long getTime() {
    if (this.runningState == STATE_STOPPED) {
        return stopTime - startTime; // The stopwatch has stopped, so this is the elapsed time.
    } else if (this.runningState == STATE_SUSPENDED) {
        return this.elapsedTime; // Return the current elapsed time when suspended.
    } else { // This should only happen when running
        long currentTime = System.currentTimeMillis();
        return (currentTime - startTime);
    }
}