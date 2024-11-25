public void stop() {
    if(this.runningState == STATE_RUNNING) {
        stopTime = System.currentTimeMillis();
        suspendTime = System.currentTimeMillis() - startTime + resumeTime;
        this.runningState = STATE_STOPPED;
    } else if (this.runningState == STATE_SUSPENDED) {
        stopTime = System.currentTimeMillis();
        suspendTime = System.currentTimeMillis() - startTime + resumeTime + (stopTime - resumeTime);
        this.runningState = STATE_STOPPED;
    } else {
        throw new IllegalStateException("Stopwatch is not running. ");
    }
}