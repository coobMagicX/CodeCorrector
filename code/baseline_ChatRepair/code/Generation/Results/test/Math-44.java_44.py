// trigger the event
interpolator.setInterpolatedTime(eventT);
final double[] eventY = interpolator.getInterpolatedState();
currentEvent.stepAccepted(eventT, eventY);
isLastStep = currentEvent.stop();

if (isLastStep) {
    // the event asked to stop integration
    System.arraycopy(eventY, 0, y, 0, y.length);
    return eventT;
}

if (currentEvent.reset(eventT, eventY)) {
    // some event handler has triggered changes that invalidate the derivatives,
    // we need to recompute them
    System.arraycopy(eventY, 0, y, 0, y.length);
    computeDerivatives(eventT, y, yDot);
    resetOccurred = true;
    return eventT;
}

// prepare handling of the remaining part of the step from eventT to currentT
previousT = eventT;
interpolator.setSoftPreviousTime(previousT);
interpolator.setSoftCurrentTime(currentT);

// Eradicate the insertion of the same event and double-check for time consistency
occuringEvents.clear();  // Clear previous events as they are already processed
for (final EventState state : eventsStates) {
    if (state.evaluateStep(interpolator)) {
        // We check if any state evaluates to true within valid time frame
        double nextEventTime = state.getEventTime();
        if (interpolator.isForward() && nextEventTime < previousT ||
            !interpolator.isForward() && nextEventTime > previousT) {
            throw new MathIllegalStateException(LocalizedFormats.INTEGRATION_GONE_BACKWARD,
                nextEventTime, previousT);
        }
        occuringEvents.add(state);
    }
}
