public void stop() {
    if (this.runningState != STATE_RUNNING && this.runningState != STATE_SUSPENDED) {
        throw new IllegalStateException("Stopwatch is not running.");
    }

    long currentTime = System.currentTimeMillis();

    if (this.runningState == STATE_RUNNING) {
        totalTime += currentTime - startTime; // Accumulate total time from last start
    } else if (this.runningState == STATE_SUSPENDED) {
        // No time should be added since time wasn't flowing, confirm requirements
        // Typically you wouldn't add time if it's suspended since no "run time" is counting
    }

    stopTime = currentTime; // Stop time is set when