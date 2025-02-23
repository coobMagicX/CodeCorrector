public void stop() {
    if (this.runningState != STATE_RUNNING && this.runningState != STATE_SUSPENDED) {
        throw new IllegalStateException("Stopwatch is not running.");
    }
    
    if (this.runningState == STATE_RUNNING) {
        stopTime = System.currentTimeMillis();
        totalTime += stopTime - startTime;
    } // If the stopwatch is suspended, the total time has already been calculated at the time of suspension
    
    this.runningState = STATE_STOPPED;
}
