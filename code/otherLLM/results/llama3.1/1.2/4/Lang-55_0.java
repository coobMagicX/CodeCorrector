public void stop() {
    if(this.runningState != STATE_RUNNING && this.runningState != STATE_SUSPENDED) {
        throw new IllegalStateException("Stopwatch is not running. ");
    }
    suspendTime = System.currentTimeMillis(); // Record the time when suspended
    stopTime = System.currentTimeMillis();
    totalElapsedTime = (stopTime - start_time); // Correctly calculate total elapsed time 
    this.runningState = STATE_STOPPED;
}