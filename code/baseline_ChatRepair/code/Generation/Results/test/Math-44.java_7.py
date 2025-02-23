protected double acceptStep(final AbstractStepInterpolator interpolator,
                            final double[] y, final double[] yDot, final double tEnd)
    throws MathIllegalStateException {

        double previousT = interpolator.getGlobalPreviousTime();
        final double currentT = interpolator.getGlobalCurrentTime();
        resetOccurred = false;

        // initialize the events states if needed
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
                previousT = eventT; // Ensure previousT is updated after reset
                interpolator.setSoftPreviousTime(eventT); // Set softPreviousTime for a consistent timeline
                interpolator.setSoftCurrentTime(currentT);
                break; // Exit loop to handle reset scenario properly
            }

            previousT = eventT; // Move forward to the next event time after handling an event
        }

        // No more occurring events in the step, finish the integration step
        interpolator.setInterpolatedTime(currentT);
        final double[] currentY = interpolator.getInterpolatedState();
        for (final EventState state : eventsStates) {
            state.stepAccepted(currentT, currentY);
            isLastStep = isLastStep || state.stop();
        }

        for (StepHandler handler : stepHandlers) {
            handler.handleStep(interpolator, isLastStep);
        }

        return currentT;
}
