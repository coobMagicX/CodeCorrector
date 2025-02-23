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

    while (true) {
        SortedSet<EventState> occurringEvents = findOccurringEvents(interpolator);
        if (occurringEvents.isEmpty()) {
            break;
        }

        // handle the first occurring event
        EventState currentEvent = occurringEvents.first();
        double eventT = currentEvent.getEventTime();
        interpolator.setSoftPreviousTime(previousT);
        interpolator.setSoftCurrentTime(eventT);
        interpolator.setInterpolatedTime(eventT);
        double[] eventY = interpolator.getInterpolatedState().clone();

        currentEvent.stepAccepted(eventT, eventY);
        boolean stop = currentEvent.stop();
        if (stop) {
            System.arraycopy(eventY, 0, y, 0, y.length);
            return eventT;
        }

        boolean needReset = currentEvent.reset(eventT, eventY);
        if (needReset) {
            System.arraycopy(eventY, 0, y, 0, y.length);
            computeDerivatives(eventT, y, yDot);
            return eventT;
        }

        previousT = eventT;
        interpolator.setSoftPreviousTime(eventT);
        interpolator.setSoftCurrentTime(currentT);
        reinitializeFutureEvents(eventT, eventY);
    }

    interpolator.setInterpolatedTime(currentT);
    double[] currentY = interpolator.getInterpolatedState();
    for (EventState state : eventsStates) {
        state.stepAccepted(currentT, currentY);
        if (state.stop()) {
            return currentT;
        }
    }

    return currentT;
}

private SortedSet<EventState> findOccurringEvents(AbstractStepInterpolator interpolator) {
    SortedSet<EventState> occurringEvents = new TreeSet<>(new Comparator<EventState>() {
        public int compare(EventState es0, EventState es1) {
            int orderingSign = interpolator.isForward() ? +1 : -1;
            return orderingSign * Double.compare(es0.getEventTime(), es1.getEventTime());
        }
    });

    for (EventState state : eventsStates) {
        if (state.evaluateStep(interpolator)) {
            occurringEvents.add(state);
        }
    }
    return occurringEvents;
}

private void reinitializeFutureEvents(double eventT, double[] eventY) {
    for (EventState state : eventsStates) {
        if (!state.stepAccepted(eventT, eventY)) {
            continue;
        }
        state.reset(eventT, eventY);
    }
}
