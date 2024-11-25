public void stop() {
    if(this.runningState == STATE_SUSPENDED) {
        stopTime = System.currentTimeMillis();
    }
    this.runningState = STATE_STOPPED;
}