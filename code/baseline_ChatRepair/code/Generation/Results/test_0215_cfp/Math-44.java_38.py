protected double acceptStep(final AbstractStepInterpolator interpolator,
                            final double[] y, final double[] yDot, final double tEnd)
    throws MathIllegalStateException {

    double previousT = interpolator.getGlobalPreviousTime();
    final double currentT = interpolator.getGlobalCurrentTime();
    resetOccurred = false;

    if (!statesInitialized) {
        for (EventState state : eventsStates) {
            state.reinitializeBegin(interpolator);
        }
        statesInitialized = true;
    }

    final int orderingSign = interpolator.isForward() ? +1 : -1;
    SortedSet<EventState> occurringEvents = new TreeSet<EventState>(new Comparator<EventState>() {
        public int compare(EventState es0, EventState es1) {
            return orderingSign * Double.compare(es0.getEventTime(), es1.getEventTime());
        }
    });

    for (final EventState state : eventsStates) {
        if (state.evaluateStep(interpolator)) {
            occurringEvents.add(state);
        }
    }

    while (!occurringEvents.isEmpty()) {
        final Iterator<EventState> iterator = occurringEvents.iterator();
        final EventState currentEvent = iterator.next();
        iterator.remove();

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
            previousT = eventT; // Update previousT after the reset
            interpolator.setSoftPreviousTime(eventT);
            interpolator.setSoftCurrentTime(currentT);
        } else {
            previousT = eventT; // Ensure previousT is always updated after an event
        }

        if (currentEvent.evaluateStep(interpolator)) {
            occurringEvents.add(currentEvent);
        }
    }

    interpolator.setInterpolatedTime(currentT);
    for (final EventState state : eventsStates) {
        state.stepAccepted(currentT, interpolator.getInterpolatedState());
        isLastStep = isLastStep || state.stop();
    }
    isLastStep = isLastStep || Precision.equals(currentT, tEnd, 1);

    for (StepHandler handler : stepHandlers) {
        handler.handleStep(interpolator, isLastStep);
    }

    return currentT;
}
