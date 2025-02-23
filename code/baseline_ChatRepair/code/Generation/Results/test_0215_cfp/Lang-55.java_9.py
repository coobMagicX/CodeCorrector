public void stop() {
    if (this.runningState != STATE_RUNNING && this.runningState != STATE_SUSPENDED) {
        throw new IllegalStateException("Stopwatch is not running.");
    }
    
    // Setting stopTime should only happen when the stopwatch transitions from running, or suspended.
    if (this.runningState == STATE_RUNNING || this.runningState == STATE_SUSPENDED) {
        stopTime = System.currentTimeMillis();
    }
    
    // Further