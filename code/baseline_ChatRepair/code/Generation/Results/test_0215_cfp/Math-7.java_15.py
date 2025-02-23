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

        // Sorted set to hold upcoming events properly ordered
        SortedSet<EventState> occurringEvents = new TreeSet<>(new Comparator<EventState>() {
            @Override // Correctly use override annotation for interface methods
            public int compare(EventState es0, EventState es1) {
                return interpolator.isForward() ? Double.compare(es0.getEventTime(), es1.getEventTime()) :
                                                 Double.compare(es1.getEventTime(), es0.getEventTime());
            }
        });

        for (final EventState state : eventsStates) {
            if (state.evaluateStep(interpolator)) { // Check if any events should occur during this step
                occurringEvents.add(state); // If yes, add them to the sorted set
            }
        }

        while (!occurringEvents.isEmpty()) {
            final EventState currentEvent = occurringEvents.first();
            occurringEvents.remove(currentEvent);

            final double eventT = currentEvent.getEventTime();
            interpolator.setSoftPreviousTime(previousT);
            interpolator.setSoftCurrentTime(eventT);

            interpolator.setInterpolatedTime(eventT);
            final double[] eventY = interpolator.getInterpolatedState().clone();

            currentEvent.stepAccepted(eventT, eventY);
            boolean isLastStep = currentEvent.stop(); // Check if the integration should stop here

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
                for (EventState remaining : occurringEvents) {
                    remaining.stepAccepted(eventT, eventY);
                }
                return eventT;
            }

            previousT = eventT; // Move to the next event in line
            
            // Re-evaluate the remaining steps to check if the event would occur again
            occurringEvents.clear();
            for (final EventState state : eventsStates) {
                if (state.evaluateStep(interpolator)) {
                    occurringEvents.add(state);
                }
            }
        }

        // Handling the final part of the step
        interpolator.setInterpolatedTime(currentT);
        final double[] currentY = interpolator.getInterpolatedState();
        for (final EventState state : eventsStates) {
            state.stepAccepted(currentT, currentY);
            if (state.stop()) {
                isLastStep = true;
            }
        }

        for (StepHandler handler : stepHandlers) {
            handler.handleStep(interpolator, isLastStep);
        }

        return currentT;
}
