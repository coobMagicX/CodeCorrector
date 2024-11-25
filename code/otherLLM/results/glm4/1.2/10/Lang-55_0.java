public void stop() {
    // If the stopwatch is not running and not suspended, throw an exception.
    if (this.runningState != STATE_RUNNING && this.runningState != STATE_SUSPENDED) {
        throw new IllegalStateException("Stopwatch is not running or has already been stopped.");
    }
    
    // Stop accumulating time by setting stopTime to the current time when in running state.
    if (this.runningState == STATE_RUNNING) {
        stopTime = System.currentTimeMillis();
    }
    
    // Set the running state to STATE_STOPPED.
    this.runningState = STATE_STOPPED;
}