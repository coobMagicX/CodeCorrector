protected double acceptStep(final AbstractStepInterpolator interpolator,
                            final double[] y, final double[] yDot, final double tEnd)
    throws MathIllegalStateException {

    double previousT = interpolator.getGlobalPreviousTime();
    final double currentT = interpolator.getGlobalCurrentTime();
    resetOccurred = false;

    // initialize the events states if needed
    if (! statesInitialized) {
        for (EventState state : eventsStates) {
            state.reinitializeBegin(interpolator);
        }
        statesInitialized = true;
    }

    // search for next events that may occur during the step
    final int orderingSign = interpolator.isForward() ? +1 : -1;
    SortedSet<EventState> occuringEvents = new TreeSet<>(new Comparator<EventState>() {
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
        // handle the chronologically first event
        final Iterator<EventState> iterator = occuringEvents.iterator();
        final EventState currentEvent = iterator.next();
        iterator.remove();

        final double eventT = currentEvent.getEventTime();

        // Avoid going backward in time:
        if (eventT < previousT) {
            throw new MathIllegalStateException("Time progression error: event time " + eventT + " is before previous time " + previousT);
        }

        interpolator.setSoftPreviousTime(previousT);
        interpolator.setSoftCurrentTime(eventT);

        // trigger the event
        interpolator.setInterpolatedTime(eventT);
        final double[] eventY = interpolator.getInterpolatedState();
        currentEvent.stepAccepted(eventT, eventY);
        isLastStep = currentEvent.stop();

        for (final StepHandler handler : stepHandlers) {
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

        previousT = eventT; // Update previousT only after processing the event successfully.
    }

    interpolator.setInterpolatedTime(currentT);
    for (final EventState state : eventsStates) {
        double[] currentY = interpolator.getInterpolatedState();
        state.stepAccepted(currentT, currentY);
        isLastStep = isLastStep || state.stop();
    }
    
    isLastStep = isLastStep || Precision.equals(currentT, tEnd, 1);

    for (StepHandler handler : stepHandlers) {
        handler.handleStep(interpolator, isLastStep);
    }

    System.arraycopy(interpolator.getInterpolatedState(), 0, y, 0, y.length);
    return currentT;
}
