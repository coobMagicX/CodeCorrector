protected double acceptStep(final AbstractStepInterpolator interpolator,
                            final double[] y, final double[] yDot, final double tEnd)
    throws MaxCountExceededException, DimensionMismatchException, NoBracketingException {

        double previousT = interpolator.getGlobalPreviousTime();
        final double currentT = interpolator.getGlobalCurrentTime();

        if (!statesInitialized) {
            for (EventState state : eventsStates) {
                state.reinitializeBegin(interpolator);
            }
            statesInitialized = true;
        }

        final int orderingSign = interpolator.isForward() ? +1 : -1;
        SortedSet<EventState> occurringEvents = new TreeSet<>( (es0, es1) -> 
            orderingSign * Double.compare(es0.getEventTime(), es1.getEventTime())
        );

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
            final double[] eventY = interpolator.getInterpolatedState().clone();

            currentEvent.stepAccepted(eventT, eventY);
            isLastStep = currentEvent.stop();
            handleHandlersStep(interpolator, isLastStep, eventY, y);

            if (isLastStep) {
                return finalizeStep(eventY, y, occurringEvents, eventT);
            }

            boolean needReset = currentEvent.reset(eventT, eventY);
            if (needReset) {
                System.arraycopy(eventY, 0, y, 0, y.length);
                computeDerivatives(eventT, y, yDot);
                resetOccurred = true;
                return finalizeStep(eventY, y, occurringEvents, eventT);
            }

            previousT = eventT;
            if (currentEvent.evaluateStep(interpolator)) {
                occurringEvents.add(currentEvent);
            }
        }

        return finalizeStepAfterLastEvent(interpolator, currentT, tEnd);
}
