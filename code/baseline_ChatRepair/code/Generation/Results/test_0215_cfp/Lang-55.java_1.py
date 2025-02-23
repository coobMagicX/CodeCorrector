public void stop() {
    if (this.runningState != STATE_RUNNING && this.runningState != STATE_SUSPENDED) {
        throw new IllegalStateException("Stopwatch is not running.");
    }
    
    long currentTime = System.currentTimeMillis();
    if (this.runningState == STATE_SUSPENDED) {
        // If suspended, there should be no addition to the elapsed time.
    } else if (this.runningState == STATE_RUNNING) {
        // Update total elapsed time only if the stopwatch was running.
        totalTime += currentTime - startTime;
    }
    
    stopTime = currentTime;
    this.runningState = STATE_STOPPED;
}
