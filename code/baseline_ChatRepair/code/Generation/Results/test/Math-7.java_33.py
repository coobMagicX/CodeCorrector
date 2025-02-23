protected double acceptStep(final AbstractStepInterpolator interpolator, final double[] y, final double[] yDot, final double tEnd)
    throws MaxCountExceededException, DimensionMismatchException, NoBracketingException {

        double previousT = interpolator.getGlobalPreviousTime();
        final double currentT = interpolator.getGlobalCurrentTime();

        if (!statesInitialized) {
            for (EventState state : eventsStates) {
                state.reinitializeBegin(interpolator);
            }
            statesInitialized = true;
        }

        SortedSet<EventState> occurringEvents = new TreeSet<>(new Comparator<EventState>() {
            public int compare(EventState es0, EventState es1) {
                return Integer.compare(es0.isForward() ? +1 : -1 * Double.compare(es0.getEventTime(), es1.getEventTime()));
            }
        });

        for (final EventState state : eventsStates) {
            if (state.evaluateStep(interpolator)) {
                occurringEvents.add(state);
            }
        }

        while (!occurringEvents.isEmpty()) {
            final EventState currentEvent = occurringEvents.first();
            occurringEvents.remove(currentEvent);

            final double eventT = currentEvent.getEventTime();
            interpolator.setSoftPreviousTime(previousT);
            interpolator.setSoftCurrentTime(eventT);

            interpolator.setInterpolatedTime(eventT);
            final double[] eventY = interpolator.getInterpolatedState().clone();

            currentEvent.stepAccepted(eventT, eventY);
            isLastStep = currentEvent.stop();

            for (final StepHandler handler : stepHandlers) {
                handler.handleStep(interpolator, isLastStep);
            }

            if (isLastStep) {
                System.arraycopy(eventY, 0, y, 0, y.length);
                return eventT;
            }

            boolean needReset = currentEvent.reset(eventT, eventY);
            if (needReset) {
                System.arraycopy(eventY, 0, y, 0, y.length);
                computeDerivatives(eventT, y, yDot);
                resetOccurred = true;
                // Recheck all events as system state has changed
                for (final EventState recheckState : eventsStates) {
                    if(recheckState.evaluateStep(interpolator)) occurringEvents.add(recheckState);
                }
                return eventT;
            }

            previousT = eventT;
            interpolator.setSoftPreviousTime(eventT);
            interpolator.setSoftCurrentTime(currentT);

            for (final EventState recheckState : eventsStates) {
                if (recheckState.evaluateStep(interpolator)) {
                    occurringEvents.add(recheckState);
                }
            }
        }

        interpolator.setInterpolatedTime(currentT);
        for (final EventState state : eventsStates) {
            final double[] currentY = interpolator.getInterpolatedState();
            state.stepAccepted(currentT, currentY);
            isLastStep = isLastStep || state.stop();
        }
        isLastStep = isLastStep || Precision.equals(currentT, tEnd, 1);

        for (StepHandler handler : stepHandlers) {
            handler.handleStep(interpolator, isLastStep);
        }

        return currentT;
}
