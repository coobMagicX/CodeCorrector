public void stop() {
    if(this.runningState != STATE_RUNNING && this.runningState != STATE_SUSPENDED) {
        throw new IllegalStateException("Stopwatch is not running. ");
    }
    if (this.runningState == STATE_SUSPENDED) {
        resume(); // Resume before stopping if it was suspended
    }
    stopTime = System.currentTimeMillis();
    this.runningState = STATE_STOPPED;
}