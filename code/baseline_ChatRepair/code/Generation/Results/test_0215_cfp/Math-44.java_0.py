protected double acceptStep(final AbstractStepInterpolator interpolator,
                            final double[] y, final double[] yDot, final double tEnd)
    throws MathIllegalStateException {

        double previousT = interpolator.getGlobalPreviousTime();
        final double originalCurrentT = interpolator.getGlobalCurrentTime();  // Ensure the original current time is stored and used consistently
        double currentT = originalCurrentT;  // Use a local variable that can be adjusted without altering the global perception prematurely
        resetOccurred = false;

        // Initialize the events states if needed
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
                return eventT;
            }

            // Update previousT for the next event processing loop or for post-event processing
            previousT = eventT;
            if (currentEvent.evaluateStep(interpolator)) {
                occurringEvents.add(currentEvent);
            }
        }

        interpolator.setInterpolatedTime(originalCurrentT); // Ensure we set to the original current time
        final double[] currentY = interpolator.getInterpolatedState();
        for (final EventState state : eventsStates) {
            state.stepAccepted(originalCurrentT, currentY);
            isLastStep = isLastStep || state.stop();
        }
        isLastStep = isLastStep || Precision.equals(originalCurrentT, tEnd, 1);

        for (StepHandler handler : stepHandlers) {
            handler.handleStep(interpolator, isLastStep);
        }

        return originalCurrentT; // Make sure to return the original current time
}
