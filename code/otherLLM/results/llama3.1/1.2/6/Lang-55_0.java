public void stop() {
    if(this.runningState == STATE_SUSPENDED) {
        suspensionTime += (System.currentTimeMillis() - this.suspensionStartTime);
        stopTime = System.currentTimeMillis();
    } else if(this.runningState != STATE_RUNNING && this.runningState != STATE_SUSPENDED) {
        throw new IllegalStateException("Stopwatch is not running. ");
    }
    this.runningState = STATE_STOPPED;
}