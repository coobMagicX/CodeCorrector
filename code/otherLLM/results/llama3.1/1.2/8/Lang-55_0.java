public void stop() {
    if(this.runningState != STATE_RUNNING && this.runningState != STATE_SUSPENDED) {
        throw new IllegalStateException("Stopwatch is not running. ");
    }
    long suspendTime = System.currentTimeMillis() - this.startTime; // Add suspension time calculation
    stopTime = System.currentTimeMillis() + suspendTime; // Update stopTime with total elapsed time, including suspension period
    this.runningState = STATE_STOPPED;
}