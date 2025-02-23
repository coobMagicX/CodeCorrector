public void stop() {
    // Check if the stopwatch is either running or suspended.
    if (this.runningState != STATE_RUNNING && this.runningState != STATE_SUSPENDED) {
        throw new IllegalStateException("Stopwatch is not running.");
    }

    // Record the stop time only when the stopwatch is actually running or suspended.
    stopTime = System.currentTimeMillis();

    // Change the state to stopped.
    this.runningState = STATE_STOPPED;
}
