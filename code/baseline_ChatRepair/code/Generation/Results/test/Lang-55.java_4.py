public void stop() {
    if (this.runningState != STATE_RUNNING && this.runningState != STATE_SUSPENDED) {
        throw new IllegalStateException("Stopwatch is not running.");
    }
    
    long currentTime = System.currentTimeMillis();

    if (this.runningState == STATE_RUNNING) {
        totalTime += currentTime - startTime;
    }
    // If it was suspended, we calculate duration based on suspendTime
    else if (this.runningState == STATE_SUSPENDED) {
        totalTime += currentTime - suspendTime;
    }

    stopTime = currentTime;  // Update stop time to current time
    this.runningState = STATE_STOPPED;
}
