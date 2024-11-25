public void suspend() {
    if(this.runningState != STATE_RUNNING) {
        throw new IllegalStateException("Stopwatch must be running to suspend. ");
    }
    // Capture the elapsed time up until the moment of suspension and set as start time after suspension.
    this.elapsedTime = (stopTime - startTime);
    stopTime = System.currentTimeMillis();
    this.runningState = STATE_SUSPENDED;
}

public void stop() {
    if(this.runningState != STATE_RUNNING && this.runningState != STATE_SUSPENDED) {
        throw new IllegalStateException("Stopwatch is not running. ");
    }
    // If the stopwatch is suspended, the time should not change upon stopping.
    if (this.runningState == STATE_SUSPENDED) {
        startTime = stopTime; // Reset start time to the last stop time before suspension.
    } else {
        // If not suspended, calculate the elapsed time from the last recorded start time.
        this.elapsedTime += (System.currentTimeMillis() - startTime);
    }
    stopTime = System.currentTimeMillis();
    this.runningState = STATE_STOPPED;
}