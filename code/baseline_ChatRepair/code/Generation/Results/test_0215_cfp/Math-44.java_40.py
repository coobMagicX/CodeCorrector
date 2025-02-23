protected double acceptStep(final AbstractStepInterpolator interpolator,
                            final double[] y, final double[] yDot, final double tEnd)
    throws MathIllegalStateException {

    double previousT = interpolator.getGlobalPreviousTime();
    final double currentT = interpolator.getGlobalCurrentTime();
    double newCurrentT = currentT; // Variable to track actual current time after handling events
    resetOccurred = false;

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
        /** {@inheritDoc} */
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

        final double eventT = currentEvent.getEventTime();
        interpolator.setSoftPreviousTime(previousT);
        interpolator.setSoftCurrentTime(eventT);

        // Trigger the event
        interpolator.setInterpolatedTime(eventT);
        final double[] eventY = interpolator.getInterpolatedState();
        currentEvent.stepAccepted(eventT, eventY);
        isLastStep = currentEvent.stop();

        // Handle the first part of the step, up to the event
        handleStep(interpolator, isLastStep);

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

        // Prepare handling of the remaining part of the step
        previousT = eventT;  // Correctly update previousT

        interpolator.setSoftPreviousTime(eventT);
        interpolator.setSoftCurrentTime(newCurrentT);

        // Recheck if the same event occurs again in the remaining part of the step
        if (currentEvent.evaluateStep(interpolator)) {
            occurringEvents.add(currentEvent);
        }
    }

    interpolator.setInterpolatedTime(newCurrentT);
    final double[] currentY = interpolator.getInterpolatedState();
    completeStep(newCurrentT, currentY);

    return newCurrentT;
}

private void handleStep(AbstractStepInterpolator interpolator, boolean isLastStep) {
    // Custom method deals with step handling to avoid repetition
}

private void completeStep(double time, double[] state) {
    // Custom method deals with completing the step 
}
