protected double acceptStep(final AbstractStepInterpolator interpolator,
                            final double[] y, final double[] yDot, final double tEnd)
    throws MaxCountExceededException, DimensionMismatchException, NoBracketingException {

    double previousT = interpolator.getGlobalPreviousTime();
    final double currentT = interpolator.getGlobalCurrentTime();
    boolean forward = interpolator.isForward();

    // initialize the event states if needed
    if (!statesInitialized) {
        for (EventState state : eventsStates) {
            state.reinitializeBegin(interpolator);
        }
        statesInitialized = true;
    }

    // Search for next events that may occur during the step
    ScheduledEventsHandler eventsHandler = new ScheduledEventsHandler(eventsStates, interpolator, forward);

    // Process events chronologically
    while (eventsHandler.hasPendingEvents()) {
        EventOccurrence occurrence = eventsHandler.getNextEvent();

        // Restrict the interpolator to the first part of the step, up to the event
        interpolator.setSoftPreviousTime(previousT);
        interpolator.setSoftCurrentTime(occurrence.getTime());

        // Update interpolator and state at event time
        interpolator.setInterpolatedTime(occurrence.getTime());
        double[] eventY = interpolator.getInterpolatedState().clone();

        // Advance all event states to current time and process step acceptance
        occurrence.processEvent(eventY);

        // Check if the event requests stopping the integration
        if (occurrence.shouldStopIntegration()) {
            System.arraycopy(eventY, 0, y, 0, y.length);
            return occurrence.getTime();
        }

        // Check if the state needs to be reset due to the event
        if (occurrence.stateResetRequired()) {
            System.arraycopy(eventY, 0, y, 0, y.length);
            computeDerivatives(occurrence.getTime(), y, yDot);
            resetOccurred = true;
            return occurrence.getTime();
        }

        // Prepare for next part of the step
        previousT = occurrence.getTime();
        interpolator.setSoftPreviousTime(occurrence.getTime());
        interpolator.setSoftCurrentTime(currentT);

        // Re-evaluate current event for the remaining part of the step
        eventsHandler.evaluateCurrentEvent();
    }

    // Process the last part of the step after handling all events
    handleFinalStepPart(interpolator, currentT, tEnd, y);
    return currentT;
}

private void handleFinalStepPart(AbstractStepInterpolator interpolator, double currentT, double tEnd, double[] y) {
    interpolator.setInterpolatedTime(currentT);
    double[] currentY = interpolator.getInterpolatedState();
    for (EventState state : eventsStates) {
        state.stepAccepted(currentT, currentY);
        isLastStep = isLastStep || state.stop();
    }
    isLastStep = isLastStep || Precision.equals(currentT, tEnd, 1);

    // Handle the final part of the step
    for (StepHandler handler : stepHandlers) {
        handler.handleStep(interpolator, isLastStep);
    }
}
