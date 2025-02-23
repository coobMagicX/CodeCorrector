protected double acceptStep(final AbstractStepInterpolator interpolator,
                            final double[] y, final double[] yDot, final double tEnd)
    throws MathIllegalStateException {

        double previousT = interpolator.getGlobalPreviousTime();
        final double initialCurrentT = interpolator.getGlobalCurrentTime();
        double currentT = initialCurrentT; // Update currentT dynamically
        resetOccurred = false;

        // initialize the events states if needed
        if (!statesInitialized) {
            for (EventState state : eventsStates) {
                state.reinitializeBegin(interpolator);
            }
            statesInitialized = true;
        }

        // search for next events that may occur during the step
        final int orderingSign = interpolator.isForward() ? +1 : -1;
        SortedSet<EventState> occurringEvents = new TreeSet<EventState>(new Comparator<EventState>() {
            /** {@inheritDoc} */
            public int compare(EventState es0, EventState es1) {
                return orderingSign * Double.compare(es0.getEventTime(), es1.getEventTime());
            }
        });

        for (final EventState state : eventsStates) {
            if (state.evaluateStep(interpolator)) {
                // the event occurs during the current step
                occurringEvents.add(state);
            }
        }

        while (!occurringEvents.isEmpty()) {
            // handle the chronologically first event
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
                currentT = eventT; // Set the current time up to the event time just handled
                return eventT;
            }

            previousT = eventT; // update previousT to the event time

            interpolator.setSoftPreviousTime(eventT);
            interpolator.setSoftCurrentTime(initialCurrentT); // revert to initial current time from the step

            if (currentEvent.evaluateStep(interpolator)) {
                occurringEvents.add(currentEvent);
            }
        }

        interpolator.setInterpolatedTime(initialCurrentT);
        final double[] currentY = interpolator.getInterpolatedState();
        for (final EventState state : eventsStates) {
            state.stepAccepted(initialCurrentT, currentY);
            isLastStep |= state.stop();
        }

        for (StepHandler handler : stepHandlers) {
            handler.handleStep(interpolator, isLastStep);
        }

        return initialCurrentT;
}
