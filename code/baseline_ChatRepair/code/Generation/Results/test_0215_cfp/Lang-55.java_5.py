public void stop() {
    if (this.runningState != STATE_RUNNING && this.runningState != STATE_SUSPENDED) {
        throw new IllegalStateException("Stopwatch is not running.");
    }
    
    if (this.runningState == STATE_RUNNING) {
        stopTime = System.currentTimeMillis();
    } else if (this.runningState == STATE_SUSPENDED) {
        // Calculate the elapsed time until it was suspended and update the stopTime accordingly
        stopTime = suspendTime;
    }
    
    long elapsedTime = stopTime - startTime;
    totalTime += elapsedTime; 