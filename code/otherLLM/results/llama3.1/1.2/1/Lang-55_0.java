public void stop() {
    if(this.runningState != STATE_RUNNING && this.runningState != STATE_SUSPENDED) {
        throw new IllegalStateException("Stopwatch is not running. ");
    }
    stopTime = System.currentTimeMillis();
    suspendedTime += (stopTime - resumeTime);
    this.runningState = STATE_STOPPED;
}