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

        // search for next events that may occur during the step
        final int orderingSign = interpolator.isForward() ? +1 : -1;
        TreeSet<EventState> occuringEvents = new TreeSet<>(Comparator.comparingDouble(es -> es.getEventTime() * orderingSign));

        for (final EventState state : eventsStates) {
            if (state.evaluateStep(interpolator)) {
                // the event occurs during the current step
                occuringEvents.add(state);
            }
        }

        while (!occuringEvents.isEmpty()) {

            // handle the chronologically first event
            final Iterator<EventState> iterator = occuringEvents.iterator();
            final EventState currentEvent = iterator.next();
            iterator.remove();

            // restrict the interpolator to the first part of the step, up to the event
            final double eventT = currentEvent.getEventTime();
            interpolator.setSoftPreviousTime(previousT);
            interpolator.setSoftCurrentTime(eventT);

            // get state at event time
            interpolator.setInterpolatedTime(eventT);
            final double[] eventY = interpolator.getInterpolatedState().clone();

            // advance all event states to current time
            currentEvent.stepAccepted(eventT, eventY);
            boolean isLastStep = currentEvent.stop();

            // handle the first part of the step, up to the event
            for (final StepHandler handler : stepHandlers) {
                handler.handleStep(interpolator, isLastStep);
            }

            if (isLastStep) {
                // the event asked to stop integration
                System.arraycopy(eventY, 0, y, 0, y.length);
                for (EventState remaining : occuringEvents) {
                    remaining.stepAccepted(eventT, eventY);
                }
                return eventT;
            }

            boolean needReset = currentEvent.reset(eventT, eventY);
            if (needReset) {
                // some event handler has triggered changes that
                // invalidate the derivatives, we need to recompute them
                System.arraycopy(eventY, 0, y, 0, y.length);
                computeDerivatives(eventT, y, yDot);
                resetOccurred = true;
                for (EventState remaining : occuringEvents) {
                    remaining.stepAccepted(eventT, eventY);
                }
                return eventT;
            }

            // prepare handling of the remaining part of the step
            previousT = eventT;
            interpolator.setSoftPreviousTime(previousT);
            interpolator.setSoftCurrentTime(currentT);

            // check if the same event occurs again in the remaining part of the step
            if (currentEvent.evaluateStep(interpolator)) {
                // the event occurs during the current step
                occuringEvents.add(currentEvent);
            }

        }

        // last part of the step, after the last event
        interpolator.setInterpolatedTime(currentT);
        final double[] currentY = interpolator.getInterpolatedState();
        for (final EventState state : eventsStates) {
            state.stepAccepted(currentT, currentY);
            isLastStep |= state.stop();
        }
        isLastStep |= Precision.equals(currentT, tEnd, 1);

        // handle the remaining part of the step, after all events if any
        for (StepHandler handler : stepHandlers) {
            handler.handleStep(interpolator, isLastStep);
        }

        System.arraycopy(currentY, 0, y, 0, y.length);
        return currentT;
}