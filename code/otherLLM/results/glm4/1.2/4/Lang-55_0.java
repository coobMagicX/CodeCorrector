public void stop() {
    if (this.runningState == STATE_RUNNING) {
        this.suspendTime = System.currentTimeMillis(); // Capture time at suspension
        this.runningState = STATE_SUSPENDED;
    }
    if (this.runningState != STATE_STOPPED) { // Check to ensure it's not already stopped
        this.stopTime = System.currentTimeMillis();
        this.totalTime += (stopTime - suspendTime); // Update totalTime by adding suspended time
        this.runningState = STATE_STOPPED;
    } else {
        throw new IllegalStateException("Stopwatch is already stopped.");
    }
}