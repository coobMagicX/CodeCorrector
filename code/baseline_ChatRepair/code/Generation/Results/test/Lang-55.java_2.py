public void stop() {
    if (this.runningState != STATE_RUNNING && this.runningState != STATE_SUSPENDED) {
        throw new IllegalStateException("Stopwatch is not running.");
    }

    long currentTime = System.currentTimeMillis();

    if (this.runningState == STATE_RUNNING) {
        stopTime = currentTime;
    } else if (this.runningState == STATE_SUSPENDED) {
        // Accumulate the suspended elapsed time to total time
        totalTime += currentTime - suspendTime;
        stopTime =