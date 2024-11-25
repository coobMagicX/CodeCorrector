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
    SortedSet<EventState> occuringEvents = new TreeSet<EventState>(new Comparator<EventState>() {
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

        // restrict the interpolator to the first part of the step, up to the event
        final double eventT = currentEvent.getEventTime();
        interpolator.setSoftPreviousTime(previousT);
        interpolator.setSoftCurrentTime(eventT);

        // trigger the event
        interpolator.setInterpolatedTime(eventT);
        final double[] eventY = interpolator.getInterpolatedState();
        currentEvent.stepAccepted(eventT, eventY);
        isLastStep = currentEvent.stop();

        // handle the first part of the step, up to the event
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

        // prepare handling of the remaining part of the step
        previousT = eventT;
        interpolator.setSoftPreviousTime(eventT);
        interpolator.setSoftCurrentTime(currentT);

        // check if the same event occurs again in the remaining part of the step
        if (currentEvent.evaluateStep(interpolator)) {
            occuringEvents.add(currentEvent);
        }

    }

    interpolator.setInterpolatedTime(currentT);
    final double[] currentY = interpolator.getInterpolatedState();
    System.arraycopy(currentY, 0, y, 0, y.length);

    isLastStep = Precision.equals(currentT, tEnd, 1) || anyEventStopsIntegration();

    // handle the remaining part of the step, after all events if any
    for (StepHandler handler : stepHandlers) {
        handler.handleStep(interpolator, isLastStep);
    }

    return currentT;
}

private boolean anyEventStopsIntegration() {
    for (EventState state : eventsStates) {
        if (state.stop()) {
            return true;
        }
    }
    return Precision.equals(currentT, tEnd, 1);
}