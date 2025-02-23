protected double acceptStep(final AbstractStepInterpolator interpolator,
                            final double[] y, final double[] yDot, final double tEnd)
    throws MathIllegalStateException {

    double previousT = interpolator.getGlobalPreviousTime();
    final double startT = previousT; // Save the initial start time
    final double currentT = interpolator.getGlobalCurrentTime();
    resetOccurred = false;

    // initialize the events states if needed
    if (!statesInitialized) {
        for (EventState state : eventsStates) {
            state.reinitializeBegin(interpolator);
        }
        statesInitialized = true;
    }

    // Event handling setup as before...
    final int orderingSign = interpolator.isForward() ? +1 : -1;
    SortedSet<EventState> occuringEvents = new TreeSet<EventState>(new Comparator<EventState>() {
        public int compare(EventState es0, EventState es1) {
            return orderingSign * Double.compare(es0.getEventTime(), es1.getEventTime());
        }
    });

    for (final EventState state : eventsStates) {
        if (state.evaluateStep(interpolator)) {
            occuringEvents.add(state);
        }
    }

    while (!occuringEvents.isEmpty()) {
        final Iterator<EventState> iterator = occuringEvents.iterator();
        final EventState currentEvent = iterator.next();
        iterator.remove();

        final double eventT = currentEvent.getEventTime();
        interpolator.setSoftPreviousTime(previousT);
        interpolator.setSoftCurrentTime(eventT);
        interpolator.setInterpolatedTime(eventT);

        final double[] eventY = interpolator.getInterpolatedState();
        
        currentEvent.stepAccepted(eventT, eventY);
        boolean isLastStep = currentEvent.stop();
        processStepHandlers(interpolator, isLastStep); 
        
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

        previousT = eventT; // Updating the previous time
        interpolator.setSoftPreviousTime(eventT);
        interpolator.setSoftCurrentTime(currentT);

        if (currentEvent.evaluateStep(interpolator)) {
            occuringEvents.add(currentEvent);
        }
    }
    
    interpolator.setInterpolatedTime(currentT); // Ensure the time is correctly set for final processing
    
    // Process the final part of the step after all events
    final double[] currentY = interpolator.getInterpolatedState();
    for (final EventState state : eventsStates) {
        state.stepAccepted(currentT, currentY);
        isLastStep = isLastStep || state.stop();
    }
    isLastStep = isLastStep || Precision.equals(currentT, tEnd, 1);
    processStepHandlers(interpolator, isLastStep);

    return currentT;
}

private void processStepHandlers(AbstractStepInterpolator interpolator, boolean isLastStep) {
    for (StepHandler handler : stepHandlers) {
        handler.handleStep(interpolator, isLastStep);
    }
}
