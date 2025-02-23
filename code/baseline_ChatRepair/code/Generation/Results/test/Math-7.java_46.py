protected double acceptStep(final AbstractStepInterpolator interpolator,
                            final double[] y, final double[] yDot, final double tEnd)
    throws MaxCountExceededException, DimensionMismatchException, NoBracketingException {

    double previousT = interpolator.getGlobalPreviousTime();
    final double currentT = interpolator.getGlobalCurrentTime();

    // Initialize the event states if needed
    if (!statesInitialized) {
        for (EventState state : eventsStates) {
            state.reinitializeBegin(interpolator);
        }
        statesInitialized = true;
    }

    final int orderingSign = interpolator.isForward() ? +1 : -1;
    PriorityQueue<EventState> occuringEvents = new PriorityQueue<>(11, new Comparator<EventState>() {
        public int compare(EventState es0, EventState es1) {
            return orderingSign * Double.compare(es0.getEventTime(), es1.getEventTime());
        }
    });

    for (final EventState state : eventsStates) {
        if (state.evaluateStep(interpolator)) {
            occuringEvents.add(state);
        }
    }

    boolean continueIntegration = true;
    while (!occuringEvents.isEmpty() && continueIntegration) {
        EventState currentEvent = occuringEvents.poll();
        final double eventT = currentEvent.getEventTime();

        interpolator.setSoftPreviousTime(previousT);
        interpolator.setSoftCurrentTime(eventT);

        interpolator.setInterpolatedTime(eventT);
        final double[] eventY = interpolator.getInterpolatedState().clone();

        currentEvent.stepAccepted(eventT, eventY);
        isLastStep = currentEvent.stop();

        for (StepHandler handler : stepHandlers) {
            handler.handleStep(interpolator.getRestrictedStep(), isLastStep);
        }

        if (isLastStep) {
            System.arraycopy(eventY, 0, y, 0, y.length);
            return eventT;
        }

        boolean needReset = currentEvent.reset(eventT, eventY);
        if (needReset) {
            System.arraycopy(eventY, 0, y, 0, y.length);
            computeDerivatives(eventT, y, yDot);
            resetOccurred = true;
            return eventT;
        }

        previousT = eventT;
        interpolator.setSoftPreviousTime(eventT);
        interpolator.setSoftCurrentTime(currentT);

        // Check the remaining events in the step after handling the current event
        for (final EventState state : eventsStates) {
            if (state.evaluateStep(interpolator)) {
                occuringEvents.add(state);
            }
        }
    }

    // Handle the steps after the last event or if there were no events
    interpolator.setInterpolatedTime(currentT);
    final double[] currentY = interpolator.getInterpolatedState().clone();
    for (final EventState state : eventsStates) {
        state.stepAccepted(currentT, currentY);
        isLastStep = isLastStep || state.stop();
    }

    isLastStep = isLastStep || Precision.equals(currentT, tEnd, 1);
    for (StepHandler handler : stepHandlers) {
        handler.handleStep(interpolator, isLastStep);
    }

    return currentT;
}
