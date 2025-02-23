// After processing each event, update and validate time order
previousT = eventT; // update previousT to the latest event time handled
interpolator.setSoftPreviousTime(eventT);
interpolator.setSoftCurrentTime(currentT);

if (previousT > currentT && interpolator.isForward()) {
    throw new MathIllegalStateException(LocalizedFormats.INTEGRATION_GONE_BACKWARD,
        previousT, currentT);
}

// check if the same event occurs again in the remaining part of the step
if (currentEvent.evaluateStep(interpolator)) {
    // the event occurs during the current step
    occuringEvents.add(currentEvent);
}
