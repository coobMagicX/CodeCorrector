public void stop() {
    if (this.runningState == STATE_RUNNING) {
        suspendTime = System.currentTimeMillis();
        this.runningState = STATE_SUSPENDED;
    } else if (this.runningState == STATE_SUSPENDED) {
        stopTime = System.currentTimeMillis();
        totalElapsedTime = suspendTime + (stopTime - suspendTime);
        this.runningState = STATE_STOPPED;
    } else {
        throw new IllegalStateException("Stopwatch is not running.");
    }
}