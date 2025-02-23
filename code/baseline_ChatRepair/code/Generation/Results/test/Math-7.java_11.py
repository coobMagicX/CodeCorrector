protected double acceptStep(final AbstractStepInterpolator interpolator,
                            final double[] y, final double[] yDot, final double tEnd)
    throws MaxCountExceededException, DimensionMismatchException, NoBracketingException {

    double previousT = interpolator.getGlobalPreviousTime();
    final double currentT = interpolator.getGlobalCurrentTime();

    // Initialize the events states if needed
    if (!statesInitialized) {
        for (EventState state : eventsStates) {
            state.reinitializeBegin(interpolator);
        }
        statesInitialized = true;
    }

    // Create comparator for ordering events by time, respecting direction of integration
    final int orderingSign = interpolator.isForward() ? +1 : -1;
    SortedSet<EventState> occurringEvents = new TreeSet<>(new Comparator<EventState>() {
        public int compare(EventState es0, EventState es1) {
            return orderingSign * Double.compare(es0.getEventTime(), es1.getEventTime());
        }
    });

    // Check for next events that may occur during the step
    for (final EventState state : eventsStates) {
        if (state.evaluateStep(interpolator)) {
            occurringEvents.add(state);
        }
    }

    while (!occurringEvents.isEmpty()) {
        final EventState currentEvent = occurringEvents.first();
        occurringEvents.remove(currentEvent);

        // Handle the first event
        final double eventT = currentEvent.getEventTime();
        interpolator.setSoftPreviousTime(previousT);
        interpolator.setSoftCurrentTime(eventT);
        interpolator.setInterpolatedTime(eventT);
        
        double[] eventY = interpolator.getInterpolatedState().clone();
        currentEvent.stepAccepted(eventT, eventY);
        isLastStep = currentEvent.stop(); // Check if event triggers termination

        // Compute derivatives if necessary and check for reset
        boolean needReset = currentEvent.reset(eventT, eventY);
        if (needReset) {
            System.arraycopy(eventY, 0, y, 0, y.length);
            computeDerivatives(eventT, y, yDot);
            resetOccurred = true;
            return eventT;
        }

        // Handle remaining part of the event
        previousT = eventT;
        interpolator.setSoftPreviousTime(eventT);
        interpolator.setSoftCurrentTime(currentT);

        // Reevaluate to check if there are additional events
        if (currentEvent.evaluateStep(interpolator)) {
            occurringEvents.add(currentEvent);
        }
    }

    // Final part of step after all events have been handled
    interpolator.setSoftPreviousTime(previousT);
    interpolator.setSoftCurrentTime(currentT);
    for (final EventState state : eventsStates) {
        state.stepAccepted(currentT, interpolator.getInterpolatedState());
        isLastStep = isLastStep || state.stop();
    }
    isLastStep = isLastStep || Precision.equals(currentT, tEnd, 1);

    // Notify all step handlers about this step
    for (StepHandler handler : stepHandlers) {
        interpolator.setInterpolatedTime(currentT);
        handler.handleStep(interpolator, isLastStep);
    }

    return currentT;
}
