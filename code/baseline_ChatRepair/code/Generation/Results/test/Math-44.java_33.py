protected double acceptStep(final AbstractStepInterpolator interpolator,
                            final double[] y, final double[] yDot, final double tEnd)
    throws MathIllegalStateException {

    double previousT = interpolator.getGlobalPreviousTime();
    final double currentT = interpolator.getGlobalCurrentTime();
    resetOccurred = false;

    // initialize the events states if needed
    if (!statesInitialized) {
        for (EventState state : eventsStates) {
            state.reinitializeBegin(interpolator);
        }
        statesInitialized = true;
    }

    // search for next events that may occur during the step
    final int orderingSign = interpolator.isForward() ? +1 : -1;
    SortedSet<EventState> occuringEvents = new TreeSet<>(new Comparator<EventState>() {
        /** {@inheritDoc} */
        public int compare(EventState es0, EventState es1) {
            return orderingSign * Double.compare(es0.getEventTime(), es1.getEventTime());
        }
    });

    for (final EventState state : eventsStates) {
        if (state.evaluateStep(interpolator)) {
            occuringEvents.add(state);
        }
    }

    while (!occuringEvents.isEmpty()) {
        final EventState currentEvent = occuringEvents.first();
        occuringEvents.remove(currentEvent);

        final double eventT = currentEvent.getEventTime();
        if (eventT < previousT) {
            throw new MathIllegalStateException("going backward in time! (" + eventT + " < " + previousT + ")");
        }

        interpolator.setSoftPreviousTime(previousT);
        interpolator.setSoftCurrentTime(eventT);

        interpolator.setInterpolatedTime(eventT);
        final double[] eventY = interpolator.getInterpolatedState();

        currentEvent.stepAccepted(eventT, eventY);
        isLastStep = currentEvent.stop();

        for (StepHandler handler : stepHandlers) {
            handler.handleStep(interpolator, isLastStep);
        }

        if (isLastStep) {
            System.arraycopy(eventY, 0, y, 0, y.length);
            return eventT;
        }

        if (currentEvent.reset(eventT, eventY)) {
            System.arraycopy(eventY, 0, y, 0, y.length);
            computeDerivatives(eventT, y, yDot);
            resetOccurred = true;
            return eventT;
        }

        previousT = eventT; // Update previousT to the last event time handled

        // Re-evaluate the remaining eventsStates since the state might have changed
        for (final EventState state : eventsStates) {
            if (state.evaluateStep(interpolator) && !occuringEvents.contains(state)) {
                occuringEvents.add(state);
            }
        }
    }

    // Once all events have been processed, set the time to currentT
    interpolator.setInterpolatedTime(currentT);
    final double[] currentY = interpolator.getInterpolatedState();
    for (final EventState state : eventsStates) {
        state.stepAccepted(currentT, currentY);
        isLastStep = isLastStep || state.stop();
    }

    for (StepHandler handler : stepHandlers) {
        handler.handleStep(interpolator, isLastStep);
    }

    isLastStep = isLastStep || Precision.equals(currentT, tEnd, 1);

    return currentT;
}
