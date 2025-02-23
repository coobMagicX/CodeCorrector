protected double acceptStep(final AbstractStepInterpolator interpolator,
                            final double[] y, final double[] yDot, final double tEnd)
    throws MaxCountExceededException, DimensionMismatchException, NoBracketingException {

    double previousT = interpolator.getGlobalPreviousTime();
    final double currentT = interpolator.getGlobalCurrentTime();
    double t = previousT; // initialize with the previous time

    if (!statesInitialized) {
        for (EventState state : eventsStates) {
            state.reinitializeBegin(interpolator);
        }
        statesInitialized = true;
    }

    boolean forward = interpolator.isForward();
    PriorityQueue<EventState> occuringEvents = new PriorityQueue<>(11,
        new Comparator<EventState>() {
            public int compare(EventState a, EventState b) {
                return forward ? Double.compare(a.getEventTime(), b.getEventTime()) :
                                Double.compare(b.getEventTime(), a.getEventTime());
            }
    });

    // Evaluate all states
    for (EventState state : eventsStates) {
        if (state.evaluateStep(interpolator)) {
            occuringEvents.add(state);
        }
    }

    boolean isLastStep = false;
    while (!occuringEvents.isEmpty() && !isLastStep) {
        EventState currentEvent = occuringEvents.poll();
        double eventT = currentEvent.getEventTime();

        interpolator.setSoftPreviousTime(previousT);
        interpolator.setSoftCurrentTime(eventT);
        interpolator.setInterpolatedTime(eventT);

        double[] eventY = interpolator.getInterpolatedState().clone();
        currentEvent.stepAccepted(eventT, eventY);
        isLastStep = currentEvent.stop();

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

        // setting up for the next part of the step
        previousT = eventT;
        if (currentEvent.evaluateStep(interpolator)) {
            occuringEvents.add(currentEvent);
        }
    }

    interpolator.setInterpolatedTime(tEnd);
    for (EventState state : eventsStates) {
        state.stepAccepted(tEnd, interpolator.getInterpolatedState());
        isLastStep = isLastStep || state.stop();
    }
    for (StepHandler handler : stepHandlers) {
        handler.handleStep(interpolator, isLastStep);
    }
    return tEnd;
}
