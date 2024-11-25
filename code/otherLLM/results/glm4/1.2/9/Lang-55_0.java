public void stop() {
    switch (this.runningState) {
        case STATE_RUNNING:
            if (this.runningState == STATE_SUSPENDED) {
                // The stopwatch is suspended, save the time and change state to stopped.
                this.suspendedTime = System.currentTimeMillis();
            } else {
                // The stopwatch is running, record the current time as stopTime.
                this.stopTime = System.currentTimeMillis();
            }
            this.runningState = STATE_STOPPED;
            break;
        case STATE_SUSPENDED:
            // If already suspended, we don't need to reset anything since it's meant to keep the state unchanged.
            break;
        default:
            throw new IllegalStateException("Stopwatch is not running or suspended. ");
    }
}