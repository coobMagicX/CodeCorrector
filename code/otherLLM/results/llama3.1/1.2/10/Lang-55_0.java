public void stop() {
    if(this.runningState != STATE_RUNNING && this.runningState != STATE_SUSPENDED) {
        throw new IllegalStateException("Stopwatch is not running. ");
    }
    long suspensionTime = System.currentTimeMillis() - suspendTime;
    totalTime += suspensionTime; // add suspended time to total time
    stopTime = System.currentTimeMillis();
    this.runningState = STATE_STOPPED;
}