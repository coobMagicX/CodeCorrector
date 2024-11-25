public void stop() {
    if (this.runningState == STATE_RUNNING || this.runningState == STATE_SUSPENDED) {
        long suspendedDuration = 0;
        
        if (this.runningState == STATE_SUSPENDED) {
            suspendedDuration = System.currentTimeMillis() - suspendedTime;
        }
        
        totalElapsed += (System.currentTimeMillis() - startTime);
        stopTime = System.currentTimeMillis();
        this.runningState = STATE_STOPPED;
    } else {
        throw new IllegalStateException("Stopwatch is not running. ");
    }
}