protected double acceptStep(final AbstractStepInterpolator interpolator,
                            final double[] y, final double[] yDot, final double tEnd)
    throws MathIllegalStateException {

        double previousT = interpolator.getGlobalPreviousTime();
        final double currentT = interpolator.getGlobalCurrentTime();
        resetOccurred = false;

        // Initialize the events states if needed
        if (!statesInitialized) {
            for (EventState state : eventsStates) {
                state.reinitializeBegin(interpolator);
            }
            statesInitialized = true;
        }

        SortedSet<EventState> occuringEvents = new TreeSet<>(new Comparator<EventState>() {
            // Define ordering based on event time considering the integration direction
            @Override
            public int compare(EventState es0, EventState es1) {
                int orderingSign = interpolator.isForward() ? +1 : -1;
                return orderingSign * Double.compare(es0.getEventTime(), es1.getEventTime());
            }
        });

        for (final EventState state : eventsStates) {
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
                return eventT; // Return after resetting to ensure step is correctly recomputed from eventT
            }

            previousT = eventT; // Set previousT to eventT after event is handled
        }

        interpolator.setSoftPreviousTime(previousT);
        interpolator.setSoftCurrentTime(currentT);
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
