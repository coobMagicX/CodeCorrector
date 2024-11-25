public void suspend() {
    if (this.runningState != STATE_RUNNING && this.runningState != STATE_SUSPENDED) {
        throw new IllegalStateException("Stopwatch is not running or already suspended.");
    }
    long suspensionTime = System.currentTimeMillis();
    this.suspensionPoint = suspensionTime;
    this.runningState = STATE_SUSPENDED;
}

public void stop() {
    if (this.runningState == STATE_RUNNING) {
        throw new IllegalStateException("Stopwatch is still running. Cannot stop while running.");
    }
    if (this.runningState == STATE_SUSPENDED) {
        // Calculate the time elapsed since suspension
        long elapsedTimeSinceSuspension = System.currentTimeMillis() - this.suspensionPoint;
        
        // Update the total elapsed time with the time since suspension
        this.totalTimeElapsed += elapsedTimeSinceSuspension;
        
        // Reset the suspension point after stopping
        this.suspensionPoint = 0;
    }
    this.stopTime = System.currentTimeMillis();
    this.runningState = STATE_STOPPED;
}