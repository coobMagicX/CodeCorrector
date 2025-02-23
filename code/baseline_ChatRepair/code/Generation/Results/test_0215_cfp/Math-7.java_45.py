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

    // Determine the direction of integration
    final int orderingSign = interpolator.isForward() ? +1 : -1;

    // A priority queue to handle events in the correct order based on step direction
    PriorityQueue<EventState> occurringEvents = new PriorityQueue<>(new Comparator<EventState>() {
        public int compare(EventState es0, EventState es1) {
            return orderingSign * Double.compare(es0.getEventTime(), es1.getEventTime());
        }
    });

    // Evaluate each event to see if it occurs during the step
    for (EventState state : eventsStates) {
        if (state.evaluateStep(interpolator)) {
            occurringEvents.add(state);
        }
    }

    while (!occurringEvents.isEmpty()) {
        // Handle the earliest occurring event
        EventState currentEvent = occurringEvents.poll();

        // Time of the event
        final double eventT = currentEvent.getEventTime();

        // Apply the event, updating the interpolator and states
        interpolator.setSoftPreviousTime(previousT);
        interpolator.setSoftCurrentTime(eventT);
        interpolator.setInterpolatedTime(eventT);
        double[] eventY = interpolator.getInterpolatedState().clone();
        
        currentEvent.stepAccepted(eventT, eventY);
        boolean isLastStep = currentEvent.stop();

        for (StepHandler handler : stepHandlers) {
            handler.handleStep(interpolator, isLastStep);
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

        // Set up for the next part of the step
        previousT = eventT;
        interpolator.setSoftPreviousTime(eventT);
        interpolator.setSoftCurrentTime(currentT);

        // Re-evaluate remaining events for the next part of the step
        if (currentEvent.evaluateStep(interpolator)) {
            occurringEvents.add(currentEvent);
        }
    }

    // After handling all events, process the last part of the step
    interpolator.setInterpolatedTime(currentT);
    double[] currentY = interpolator.getInterpolatedState().clone();
    for (EventState state : eventsStates) {
        state.stepAccepted(currentT, currentY);
        isLastStep = isLastStep || state.stop();
    }

    for (StepHandler handler : stepHandlers) {
        handler.handleStep(interpolator, isLastStep);
    }

    return currentT;
}
