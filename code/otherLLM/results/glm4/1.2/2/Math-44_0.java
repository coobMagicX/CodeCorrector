protected double acceptStep(final AbstractStepInterpolator interpolator,
                            final double[] y, final double[] yDot, final double tEnd)
    throws MathIllegalStateException {

        double previousT = interpolator.getGlobalPreviousTime();
        final double currentT = interpolator.getGlobalCurrentTime();
        resetOccurred = false;

        // initialize the events states if needed
        if (! statesInitialized) {
            for (EventState state : eventsStates) {
                state.reinitializeBegin(interpolator);
            }
            statesInitialized = true;
        }

        // search for next events that may occur during the step
        final int orderingSign = interpolator.isForward() ? +1 : -1;
        SortedSet<EventState> occuringEvents = new TreeSet<>(Comparator.comparingDouble(es -> orderingSign * es.getEventTime()));

        for (final EventState state : eventsStates) {
            if (state.evaluateStep(interpolator)) {
                // the event occurs during the current step
                occuringEvents.add(state);
            }
        }

        while (!occuringEvents.isEmpty()) {

            // handle the chronologically first event
            final Iterator<EventState> iterator = occuringEvents.iterator();
            EventState currentEvent = iterator.next();
            iterator.remove();

            // restrict the interpolator to the first part of the step, up to the event
            final double eventT = currentEvent.getEventTime();
            interpolator.setSoftPreviousTime(previousT);
            interpolator.setSoftCurrentTime(eventT);

            // trigger the event
            interpolator.setInterpolatedTime(eventT);
            final double[] eventY = interpolator.getInterpolatedState();
            currentEvent.stepAccepted(eventT, eventY);
            isLastStep = currentEvent.stop();

            // handle the first part of the step, up to the event
            for (final StepHandler handler : stepHandlers) {
                handler.handleStep(interpolator, isLastStep);
            }

            if (isLastStep) {
                // the event asked to stop integration
                System.arraycopy(eventY, 0, y, 0, y.length);
                return eventT;
            }

            if (currentEvent.reset(eventT, eventY)) {
                // some event handler has triggered changes that
                // invalidate the derivatives, we need to recompute them
                System.arraycopy(eventY, 0, y, 0, y.length);
                computeDerivatives(eventT, y, yDot);
                resetOccurred = true;
                return eventT;
            }

            previousT = eventT;
            interpolator.setSoftPreviousTime(previousT);

            // check if the same event occurs again in the remaining part of the step
            final EventState nextEvent = occuringEvents.isEmpty() ? null : occuringEvents.first();
            if (nextEvent != null && nextEvent.getEventTime() == previousT) {
                iterator.remove(); // remove the event from the set as it was already handled
            }

        }

        interpolator.setInterpolatedTime(currentT);
        final double[] currentY = interpolator.getInterpolatedState();
        for (final EventState state : eventsStates) {
            state.stepAccepted(currentT, currentY);
            isLastStep = isLastStep || state.stop();
        }
        isLastStep = isLastStep || Precision.equals(currentT, tEnd, 1);

        // handle the remaining part of the step, after all events if any
        for (final StepHandler handler : stepHandlers) {
            handler.handleStep(interpolator, isLastStep);
        }

        return currentT;

}