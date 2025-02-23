// Iterate over events and find those occurring within the step
for (final EventState state : eventsStates) {
    if (state.evaluateStep(interpolator)) {
        occuringEvents.add(state);
    }
}

// Handle each event in time order
while (!occuringEvents.isEmpty()) {
    final Iterator<EventState> iterator = occuringEvents.iterator();
    final EventState currentEvent = iterator.next();
    iterator.remove();

    final double eventT = currentEvent.getEventTime();
    interpolator.setSoftPreviousTime(previousT);
    interpolator.setSoftCurrentTime(eventT);
    interpolator.setInterpolatedTime(eventT);
    final double[] eventY = interpolator.getInterpolatedState().clone();

    // Advance event state to the current event
    currentEvent.stepAccepted(eventT, eventY);
    isLastStep = currentEvent.stop();

    // Handle step until the event
    for (final StepHandler handler : stepHandlers) {
        handler.handleStep(interpolator, isLastStep);
    }

    // Check if the integration should stop
    if (isLastStep) {
        System.arraycopy(eventY, 0, y, 0, y.length);
        return eventT;
    }

    // Check event for reset conditions
    boolean needReset = currentEvent.reset(eventT, eventY);
    if (needReset) {
        System.arraycopy(eventY, 0, y, 0, y.length);
        computeDerivatives(eventT, y, yDot);
        resetOccurred = true;
        return eventT;
    }

    // Update previous time for next iteration
    previousT = eventT;

    // Re-evaluate remaining step part for further events
    recheckCurrentEventForSubsequentOccurrence(interpolator, currentEvent, occuringEvents, currentT);
}

// Finalization of the step after the last event
interpolator.setInterpolatedTime(currentT);
return finalizeStep(interpolator, currentT, tEnd, y, yDot);
