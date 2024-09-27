public void stop() {
    if (this.runningState != STATE_RUNNING && this.runningState != STATE_SUSPENDED) {
        throw new IllegalStateException("Stopwatch is not running. ");
    }
    stopTime = System.currentTimeMillis();
    this.runningState = STATE_STOPPED;
}

public void resume() {
    if (this.runningState != STATE_SUSPENDED) {
        throw new IllegalStateException("Stopwatch must be suspended to resume. ");
    }
    startTime += (System.currentTimeMillis() - stopTime);
    stopTime = -1;
    this.runningState = STATE_RUNNING;
}