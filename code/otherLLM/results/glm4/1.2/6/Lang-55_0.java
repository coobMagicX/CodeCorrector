public void stop() {
    if (this.runningState == STATE_SUSPENDED) {
        // Do nothing, do not change the state or stop time when suspended.
        return;
    }

    if (this.runningState != STATE_RUNNING) {
        throw new IllegalStateException("Stopwatch is not running. ");
    }
    
    this.stopTime = System.currentTimeMillis();
    this.runningState = STATE_STOPPED;
}