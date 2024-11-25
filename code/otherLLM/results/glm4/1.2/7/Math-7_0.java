protected double acceptStep(final AbstractStepInterpolator interpolator,
                            final double[] y, final double[] yDot, final double tEnd)
    throws MaxCountExceededException, DimensionMismatchException, NoBracketingException {

        double previousT = interpolator.getGlobalPreviousTime();
        final double currentT = interpolator.getGlobalCurrentTime();

        // Correct initialization of the integrator with the appropriate step size and tolerance
        interpolator.initialize(currentT - previousT, 1e-6, 1e-10);

        // initialize the events states if needed
        if (!statesInitialized) {
            for (EventState state : eventsStates) {
                state.reinitializeBegin(interpolator);
            }
            statesInitialized = true;
        }

        // search for next events that may occur during the step
        final int orderingSign = interpolator.isForward() ? +1 : -1;
        SortedSet<EventState> occuringEvents = new TreeSet<>(Comparator.comparing(EventState::getEventTime));

        for (final EventState state : eventsStates) {
            if (state.evaluateStep(interpolator)) {
                // the event occurs during the current step
                occuringEvents.add(state);
            }
        }

        while (!occuringEvents.isEmpty()) {

            // handle the chronologically first event
            final EventState currentEvent = occuringEvents.first();
            occuringEvents.remove(currentEvent);

            // restrict the interpolator to the first part of the step, up to the event
            final double eventT = currentEvent.getEventTime();
            interpolator.setSoftPreviousTime(previousT);
            interpolator.setSoftCurrentTime(eventT);

            // get state at event time
            interpolator.setInterpolatedTime(eventT);
            final double[] eventY = interpolator.getInterpolatedState().clone();

            // advance all event states to current time
            currentEvent.stepAccepted(eventT, eventY);
            isLastStep = currentEvent.stop();

            // handle the first part of the step, up to the event
            for (final StepHandler handler : stepHandlers) {
                handler.handleStep(interpolator, isLastStep);
            }

            if (isLastStep) {
                // the event asked to stop integration
                System.arraycopy(eventY, 0, y, 0, y.length);
                break;
            }

            boolean needReset = currentEvent.reset(eventT, eventY);
            if (needReset) {
                // some event handler has triggered changes that invalidate the derivatives
                System.arraycopy(eventY, 0, y, 0, y.length);
                computeDerivatives(eventT, y, yDot);
                resetOccurred = true;
                break;
            }

            // prepare handling of the remaining part of the step
            previousT = eventT;
            interpolator.setSoftPreviousTime(eventT);
            interpolator.setSoftCurrentTime(currentT);

            // check if the same event occurs again in the remaining part of the step
            if (currentEvent.evaluateStep(interpolator)) {
                occuringEvents.add(currentEvent);
            }

        }

        // last part of the step, after the last event
        interpolator.setInterpolatedTime(currentT);
        final double[] currentY = interpolator.getInterpolatedState();
        for (final EventState state : eventsStates) {
            state.stepAccepted(currentT, currentY);
            isLastStep = isLastStep || state.stop();
        }
        isLastStep = isLastStep || Precision.equals(currentT, tEnd, 1);

        // handle the remaining part of the step, after all events if any
        for (StepHandler handler : stepHandlers) {
            handler.handleStep(interpolator, isLastStep);
        }

        System.arraycopy(currentY, 0, y, 0, y.length); // Ensure y is updated with the state at tEnd

        return currentT;
}