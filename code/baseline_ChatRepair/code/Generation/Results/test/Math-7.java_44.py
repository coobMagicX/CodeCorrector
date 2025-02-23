protected double acceptStep(final AbstractStepInterpolator interpolator, final double[] y, final double[] yDot, final double tEnd)
    throws MaxCountExceededException, DimensionMismatchException, NoBracketingException {

    double previousT = interpolator.getGlobalPreviousTime();
    final double currentT = interpolator.getGlobalCurrentTime();

    if (!statesInitialized) {
        for (EventState state : eventsStates) {
            state.reinitializeBegin(interpolator);
        }
        statesInitialized = true;
    }

    final int orderingSign = interpolator.isForward() ? +1 : -1;
    SortedSet<EventState> occuringEvents = new TreeSet<>(new Comparator<EventState>() {
        public int compare(EventState es0, EventState es1) {
            int timeComparison = Double.compare(es0.getEventTime(), es1.getEventTime());
            if (timeComparison == 0) {
                return Integer.compare(es0.hashCode(), es1.hashCode());
            }
            return orderingSign * timeComparison;
        }
    });

    for (EventState state : eventsStates) {
        if (state.evaluateStep(interpolator)) {
            occuringEvents.add(state);
        }
    }

    while (!occuringEvents.isEmpty()) {
        final EventState currentEvent = occuringEvents.first();
        occuringEvents.remove(currentEvent);

        final double eventT = currentEvent.getEventTime();
        interpolator.setSoftPreviousTime(previousT);
        interpolator.setSoftCurrentTime(eventT);
        interpolator.setInterpolatedTime(eventT);

        final double[] eventY = interpolator.getInterpolatedState().clone();
        currentEvent.stepAccepted(eventT, eventY);
        isLastStep = currentEvent.stop();
        stepHandlers.forEach(handler -> handler.handleStep(interpolator, isLastStep));

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

        if (currentEvent.evaluateStep(interpolator)) {
            occuringEvents.add(currentEvent);
        }
    }

    interpolator.setInterpolatedTime(currentT);
    final double[] currentY = interpolator.getInterpolatedState();
    eventsStates.forEach(state -> {
        state.stepAccepted(currentT, currentY);
        isLastStep = isLastStep || state.stop();
    });
    isLastStep = isLastStep || Math.abs(currentT - tEnd) <= 1e-9;

    stepHandlers.forEach(handler -> handler.handleStep(interpolator, isLastStep));

    return currentT;
}
