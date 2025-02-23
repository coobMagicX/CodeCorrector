protected double acceptStep(final AbstractStepInterpolator interpolator,
                            final double[] y, final double[] yDot, final double tEnd)
    throws MaxCountExceededException, DimensionMismatchException, NoBracketingException {

        double previousT = interpolator.getGlobalPreviousTime();
        final double currentT = interpolator.getGlobalCurrentTime();

        // Initialize event states if needed
        if (!statesInitialized) {
            for (EventState state : eventsStates) {
                state.reinitializeBegin(interpolator);
            }
            statesInitialized = true;
        }

        // Search for next events that may occur during the step
        final int orderingSign = interpolator.isForward() ? +1 : -1;
        SortedSet<EventState> occurringEvents = new TreeSet<>(new Comparator<EventState>() {
            @Override
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
            interpolator.setSoftPreviousTime(previousT);
            interpolator.setSoftCurrentTime(eventT);
            interpolator.setInterpolatedTime(eventT);
            final double[] eventY = interpolator.getInterpolatedState().clone();
            currentEvent.stepAccepted(eventT, eventY);
            isLastStep = currentEvent.stop();
            
            for (final StepHandler handler : stepHandlers) {
                handler.handleStep(interpolator, isLastStep);
            }

            if (needEventReset(currentEvent, eventT, eventY)) {
                performStateReset(eventT, y, yDot, eventY);
                checkOccurrences(occurringEvents, interpolator, eventT);
                return eventT;
            }

            previousT = eventT;
            interpolator.setSoftPreviousTime(eventT);
            interpolator.setSoftCurrentTime(currentT);

            if (currentEvent.evaluateStep(interpolator)) {
                occurringEvents.add(currentEvent);
            }
        }

        handleFinalPart(interpolator, currentT, tEnd, y);
        return currentT;
}

private boolean needEventReset(EventState event, double eventT, double[] eventY) {
    return event.reset(eventT, eventY);
}

private void performStateReset(double eventT, double[] y, double[] yDot, double[] eventY) {
    System.arraycopy(eventY, 0, y, 0, eventY.length);
    computeDerivatives(eventT, y, yDot);
    resetOccurred = true;
}

private void checkOccurrences(SortedSet<EventState> events, AbstractStepInterpolator interpolator, double eventT) {
    for (final EventState remaining : events) {
        remaining.stepAccepted(eventT, interpolator.getInterpolatedState());
    }
}

private void handleFinalPart(AbstractStepInterpolator interpolator, double currentT, double tEnd, double[] y) {
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
}
