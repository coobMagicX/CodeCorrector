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
        // Handle the chronologically first event
        final Iterator<EventState> iterator = occurringEvents.iterator();
        final EventState currentEvent = iterator.next();
        iterator.remove();

        // Restrict the interpolator to the first part of the step, up to the event
        final double eventT = currentEvent.getEventTime();
        interpolator.setSoftPreviousTime(previousT);
        interpolator.setSoftCurrentTime(eventT);

        // Get state at event time
        interpolator.setInterpolatedTime(eventT);
        double[] eventY = interpolator.getInterpolatedState().clone();

        // Advance all event states to current time
        currentEvent.stepAccepted(eventT, eventY);
        boolean isLastStep = currentEvent.stop();

        // Handle the first part of the step, up to the event
        for (final StepHandler handler : stepHandlers) {
            interpolator.setInterpolatedTime(eventT); // Ensure the interpolator time is set correctly for handlers
            handler.handleStep(interpolator, isLastStep);
        }

        if (isLastStep) {
            System.arraycopy(eventY, 0, y, 0, y.length);
            return eventT;
        }

        boolean needReset = currentEvent.reset(eventT, eventY.clone());  // Ensure changes do not propagate unintentionally
        if (needReset) {
            System.arraycopy(eventY, 0, y, 0, y.length);
            computeDerivatives(eventT, y, yDot);
            return eventT;
        }

        // Prepare handling of the remaining part of the step
        previousT = eventT;

        // Recheck if the event occurs again in the remaining part of the step in case of a state change
        if (currentEvent.evaluateStep(interpolator)) {
            occurringEvents.add(currentEvent);
        }
    }

    // Last part of the step, after the last event
    interpolator.setInterpolatedTime(currentT);
    final double[] currentY = interpolator.getInterpolatedState();
    for (final EventState state : eventsStates) {
        state.stepAccepted(currentT, currentY);
        isLastStep = isLastStep || state.stop();
    }
    for (StepHandler handler : stepHandlers) {
        // Set the interpolator time and handle the final step
        interpolator.setInterpolatedTime(currentT);
        handler.handleStep(interpolator, isLastStep);
    }

    System.arraycopy(currentY, 0, y, 0, y.length);
    return currentT;
}
