public void stop() {
    if(this.runningState != STATE_RUNNING && this.runningState != STATE_SUSPENDED) {
        throw new IllegalStateException("Stopwatch is not running.");
    }
    if (this.runningState == STATE_SUSPENDED) {
        // Calculate stopped time by excluding the suspended time
        stopTime = System.currentTimeMillis() - suspendTime;
    } else {
        stopTime = System.currentTimeMillis();
    }
    this.runningState = STATE_STOP