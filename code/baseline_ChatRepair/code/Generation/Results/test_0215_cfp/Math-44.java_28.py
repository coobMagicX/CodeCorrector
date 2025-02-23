protected double acceptStep(final AbstractStepInterpolator interpolator,
                            final double[] y, final double[] yDot, final double tEnd)
    throws MathIllegalStateException {
    
    double previousT = interpolator.getGlobalPreviousTime();
    final double currentT = interpolator.getGlobalCurrentTime();
    resetOccurred = false;

    // Initialize the event states if needed.
    if (!statesInitialized) {
        for (EventState state : eventsStates) {
            state.reinitializeBegin(interpolator);
        }
        statesInitialized = true;
    }

    // Search for next events that may occur during the step.
    final int orderingSign = interpolator.isForward() ? +1 : -1;
    SortedSet<EventState> occurringEvents = new TreeSet<>(new Comparator<EventState>() {
        public int compare(EventState es0, EventState es1) {
            return orderingSign * Double.compare(es0.getEventTime(), es1.getEventTime());
        }
    });

    for (EventState state : eventsStates) {
        if (state.evaluateStep(interpolator)) {
            occurringEvents.add(state);
        }
    }

    while (!occurringEvents.isEmpty()) {
        Iterator<EventState> iterator = occurringEvents.iterator();
        EventState currentEvent = iterator.next();
        iterator.remove();

        double eventT = currentEvent.getEventTime();
        if (eventT < previousT) {
            throw new MathIllegalStateException("Going backward in time! event time: " + eventT + " is less than previous time: " + previousT);
        } 

        interpolator.setSoftPreviousTime(previousT);
        interpolator.setSoftCurrentTime(eventT);

        interpolator.setInterpolatedTime(eventT);
        double[] eventY = interpolator.getInterpolatedState();
        currentEvent.stepAccepted(eventT, eventY);
        isLastStep = currentEvent.stop();

        for (StepHandler handler : stepHandlers) {
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
        interpolator.setSoftCurrentTime(currentT);

        if (currentEvent.evaluateStep(interpolator)) {
            occurringEvents.add(currentEvent);
        }
    }

    interpolator.setInterpolatedTime(currentT);
    double[] currentY = interpolator.getInterpolatedState();
    for (EventState state : eventsStates) {
        state.stepAccepted(currentT, currentY);
        isLastStep = isLastStep || state.stop();
    }

    isLastStep = isLastStep || Precision.equals(currentT, tEnd, 1);

    for (StepHandler handler : stepHandlers) {
        handler.handleStep(interpolator, isLastStep);
    }

    return currentT;
}
