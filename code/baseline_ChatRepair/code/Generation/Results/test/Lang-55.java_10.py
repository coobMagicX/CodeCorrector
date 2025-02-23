public void stop() {
    if(this.runningState != STATE_RUNNING && this.runningState != STATE_SUSPENDED) {
        throw new IllegalStateException("Stopwatch is not running.");
    }
    
    long currentTime = System.currentTimeMillis();

    if (this.runningState == STATE_RUNNING) {
        totalTime += currentTime - startTime;
    } else if (this.runningState == STATE_SUSPENDED) {
        suspendTime += currentTime - startTime;
    }

    this.runningState = STATE_STOPPED;
   