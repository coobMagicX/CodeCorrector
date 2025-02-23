while (!occuringEvents.isEmpty()) {
    // handle the chronologically first event
    final Iterator<EventState> iterator = occuringEvents.iterator();
    final EventState currentEvent = iterator.next();
    iterator.remove();
    
    // restrict the interpolator to the first part of the step, up to the event
    final double eventT = currentEvent.getEventTime();
    interpolator.setSoftPreviousTime(previousT);
    interpolator.setSoftCurrentTime(eventT);
    
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
        // some event handler has triggered changes that
        // invalidate the derivatives, we need to recompute them
        System.arraycopy(eventY, 0, y, 0, y.length);
        computeDerivatives(eventT, y, yDot);
        resetOccurred = true;
        return eventT;
    }

    // prepare handling of the remaining part of the step
    previousT = eventT;
    interpolator.setSoftPreviousTime(eventT);
    interpolator.setSoftCurrentTime(currentT);

    // Recalculate the new current time for future validation
    currentT = interpolator.getGlobalCurrentTime();

    // check if the same event occurs again in the remaining part of the step
    if (currentEvent.evaluateStep(interpolator)) {
        // the event occurs during the current step
        occuringEvents.add(currentEvent);
    }
}

interpolator.setInterpolatedTime(currentT);
final double[] currentY = interpolator.getInterpolatedState();
for (final EventState state : eventsStates) {
    state.stepAccepted(currentT, currentY);
    isLastStep = isLastStep || state.stop();
}
isLastStep = isLastStep || Precision.equals(currentT, tEnd, 1);

// Handle the remaining part of the step, after all events if any
for (StepHandler handler : stepHandlers) {
    handler.handleStep(interpolator, isLastStep);
}
return currentT;
