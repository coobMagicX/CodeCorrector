protected double acceptStep(final AbstractStepInterpolator interpolator,
                            final double[] y, final double[] yDot, final double tEnd)
    throws MaxCountExceededException, DimensionMismatchException, NoBracketingException {

    double previousT = interpolator.getGlobalPreviousTime();
    final double currentT = interpolator.getGlobalCurrentTime();

    // initialize the events states if needed
    if (!statesInitialized) {
        for (EventState state : eventsStates) {
            state.reinitializeBegin(interpolator);
        }
        statesInitialized = true;
    }

    // events that may occur during the step
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
        // First event chronologically
        final Iterator<EventState> iterator = occuringEvents.iterator();
        final EventState currentEvent = iterator.next();
        iterator.remove();

        // set interpolator times up to the event
        final double eventT = currentEvent.getEventTime();
        interpolator.setSoftPreviousTime(previousT);
        interpolator.setSoftCurrentTime(eventT);

        // state at event time
        interpolator.setInterpolatedTime(eventT);
        final double[] eventY = interpolator.getInterpolatedState().clone();

        // update states to current time
        currentEvent.stepAccepted(eventT, eventY);
        isLastStep = currentEvent.stop();

        // handle step to event
        for (final StepHandler handler : stepHandlers) {
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

        // for remaining step part
        previousT = eventT;
        interpolator.setSoftPreviousTime(eventT);
        interpolator.setSoftCurrentTime(currentT);

        occuringEvents.removeIf(e -> !e.evaluateStep(interpolator));
    }

    // Last part of the step
    interpolator.setInterpolatedTime(currentT);
    final double[] currentY = interpolator.getInterpolatedState();
    for (final EventState state : eventsStates) {
        state.stepAccepted(currentT, currentY);
        isLastStep = isLastStep || state.stop();
    }
    isLastStep = isLastStep || Precision.equals(currentT, tEnd, 1);

    // Remaining part after all events
    for (StepHandler handler : stepHandlers) {
        handler.handleStep(interpolator, isLastStep);
    }

    return currentT;
}
