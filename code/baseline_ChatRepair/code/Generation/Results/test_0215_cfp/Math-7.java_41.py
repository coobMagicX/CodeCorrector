protected double acceptStep(final AbstractStepInterpolator interpolator,
                            final double[] y, final double[] yDot, final double tEnd)
    throws MaxCountExceededException, DimensionMismatchException, NoBracketingException {

        double previousT = interpolator.getGlobalPreviousTime();
        final double currentT = interpolator.getGlobalCurrentTime();

        // Initialize the events states if needed
        if (!statesInitialized) {
            for (EventState state : eventsStates) {
                state.reinitializeBegin(interpolator);
            }
            statesInitialized = true;
        }

        // Search for next events that may occur during the step
        final int orderingSign = interpolator.isForward() ? +1 : -1;
        SortedSet<EventState> occurringEvents = new TreeSet<>(new Comparator<EventState>() {
            public int compare(EventState es0, EventState es1) {
                return orderingSign * Double.compare(es0.getEventTime(), es1.getEventTime());
            }
        });

        boolean anyEventOccurred = false;
        for (final EventState state : eventsStates) {
            if (state.evaluateStep(interpolator)) {
                // The event occurs during the current step
                occurringEvents.add(state);
                anyEventOccurred = true;
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
            boolean isLastStep = currentEvent.stop();

            for (final StepHandler handler : stepHandlers) {
                handler.handleStep(interpolator, isLastStep);
            }

            if (isLastStep) {
                System.arraycopy(eventY, 0, y, 0, y.length);
                return eventT;
            }

            // Check if anything needs to be reset after the event
            if (currentEvent.reset(eventT, eventY)) {
                System.arraycopy(eventY, 0, y, 0, y.length);
                computeDerivatives(eventT, y, yDot);
                return eventT;
            }

            previousT = eventT;
            occurringEvents.clear();
            for (final EventState state : eventsStates) {
                if (state.evaluateStep(interpolator)) {
                    occurringEvents.add(state);
                }
            }
        }

        if (!anyEventOccurred || previousT != currentT) {
            interpolator.setSoftCurrentTime(currentT);
            interpolator.setInterpolatedTime(currentT);
            final double[] currentY = interpolator.getInterpolatedState().clone();
            for (final StepHandler handler : stepHandlers) {
                handler.handleStep(interpolator, Precision.equals(currentT, tEnd, 1));
            }
            System.arraycopy(currentY, 0, y, 0, y.length);
        }

        return currentT;
}
