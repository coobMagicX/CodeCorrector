public void stop() {
    if (this.runningState != STATE_RUNNING) {
        throw new IllegalStateException("Stopwatch is not running.");
    }
    stopTime = System.currentTimeMillis();
    this.runningState = STATE_STOPPED;
}