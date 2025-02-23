protected double acceptStep(final AbstractStepInterpolator interpolator,
                            final double[] y, final double[] yDot, final double tEnd)
    throws MaxCountExceededException, DimensionMismatchException, NoBracketingException {

        double previousT = interpolator.getGlobalPreviousTime();
        final double currentT = interpolator.getGlobalCurrentTime();

        // Initialize the events states if needed.
        if (!statesInitialized) {
            for (EventState state : eventsStates) {
                state.reinitializeBegin(interpolator);
            }
            statesInitialized = true;
        }

        boolean detectEvents;
        do {
            detectEvents = false;
            SortedSet<EventState> occurringEvents = new TreeSet<>(new Comparator<EventState>() {
                public int compare(EventState es0, EventState es1) {
                    return Double.compare(es0.getEventTime(), es1.getEventTime());
                }
            });

            // Evaluate each event state to determine if an event occurs during the step.
            for (final EventState state : eventsStates) {
                if (state.evaluateStep(interpolator)) {
                    occurringEvents.add(state);
                }
            }

            double nextEventTime = Double.POSITIVE_INFINITY;
            EventState nextEventState = null;

            if (!occurringEvents.isEmpty()) {
                nextEventState = occurringEvents.first();
                nextEventTime = nextEventState.getEventTime();
            }

            if (nextEventTime <= currentT) {
                interpolator.setSoftCurrentTime(nextEventTime);
                final double[] eventY = interpolator.getInterpolatedState().clone();
            }
            
            for (EventState state : occurringEvents) {
                state.stepAccepted(nextEventTime, eventY);
                boolean stop = state.stop();
                if (stop) {
                    System.arraycopy(eventY, 0, y, 0, y.length);
                    return nextEventTime;
                }
                
                boolean needReset = state.reset(nextEventTime, eventY);
                if (needReset) {
                    System.arraycopy(eventY, 0, y, 0, y.length);
                    computeDerivatives(nextEventTime, y, yDot);
                    resetOccurred = true;
                    detectEvents = true; // Flag to indicate re-detection of events.
                }
            }
            
            previousT = nextEventTime;
            interpolator.setSoftPreviousTime(nextEventTime);
            interpolator.setSoftCurrentTime(currentT);

        } while (detectEvents); // Re-detect events if necessary.

        // Handle the final part of the step.
        interpolator.setInterpolatedTime(currentT);
        final double[] currentY = interpolator.getInterpolatedState();
        for (final EventState state : eventsStates) {
            state.stepAccepted(currentT, currentY);
            isLastStep = isLastStep || state.stop();
        }
        isLastStep = isLastStep || Precision.equals(currentT, tEnd, 1);

        for (StepHandler handler : stepHandlers) {
            handler.handleStep(interpolator, isLastStep);
        }

        return currentT;
}
