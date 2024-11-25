public long getTime() {
    // Check if the stopwatch has been suspended and then stopped
    if (this.runningState == STATE_SUSPENDED || this.runningState == STATE_STOPPED) {
        // Return the elapsed time since the last reset or start, not the total time
        return startTime - resetTime;
    } else if (this.runningState == STATE_RUNNING) {
        // If the stopwatch is still running, calculate the elapsed time from the last reset/start to current time
        return System.currentTimeMillis() - startTime + stopTime - resetTime;
    } else {
        // If the state is neither suspended, stopped, nor running, throw an exception or handle as needed
        throw new IllegalStateException("Invalid state for getting time.");
    }
}