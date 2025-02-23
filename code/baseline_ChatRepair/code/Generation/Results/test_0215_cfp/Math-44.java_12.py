protected double acceptStep(final AbstractStepInterpolator interpolator,
                            final double[] y, final double[] yDot, final double tEnd)
    throws MathIllegalStateException {
    
    double currentT = interpolator.getGlobalCurrentTime(); // updated current time
    double previousT = interpolator.getGlobalPreviousTime(); // initial previous time
    resetOccurred = false;

    if (!statesInitialized) {
        for (EventState state : eventsStates) {
            state.reinitializeBegin(interpolator);
        }
        statesInitialized = true;
    }

    final int orderingSign = interpolator.isForward() ? +1 : -1;
    SortedSet<EventState> occuringEvents = new TreeSet<EventState>((es0, es1) -> 
        orderingSign * Double.compare(es0.getEventTime(), es1.getEventTime())
    );

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
        
        if (eventT < previousT && !Precision.equals(eventT, previousT, 1)) {
            throw new MathIllegalStateException("going backward in time! (" + eventT + " < " + previousT + ")");
        }

        interpolator.setSoftPreviousTime(previousT);
        interpolator.setSoftCurrentTime(eventT);
        interpolator.setInterpolatedTime(eventT);

        final double[] eventY = interpolator.getInterpolatedState();
        currentEvent.stepAccepted(eventT, eventY);
        isLastStep = currentEvent.stop();

        for (final StepHandler handler : stepHandlers) {
            interpolator.setInterpolatedTime(eventT);
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

        previousT = eventT;

        if (currentEvent.evaluateStep(interpolator)) {
            occuringEvents.add(currentEvent);
        }
        
        interpolator.setSoftPreviousTime(eventT);
    }

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
