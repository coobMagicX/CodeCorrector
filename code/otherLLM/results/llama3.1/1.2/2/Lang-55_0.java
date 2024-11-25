public void stop() {
    if(this.runningState != STATE_RUNNING && this.runningState != STATE_SUSPENDED) {
        throw new IllegalStateException("Stopwatch is not running. ");
    }
    stopTime = System.currentTimeMillis();
    if (this.runningState == STATE_SUSPENDED) {
        // If stopwatch was suspended, reset the start time to the current time
        startTime = System.currentTimeMillis();
    }
    this.runningState = STATE_STOPPED;
}