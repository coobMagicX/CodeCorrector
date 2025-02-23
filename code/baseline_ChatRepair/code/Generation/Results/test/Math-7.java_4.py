protected double acceptStep(final AbstractStepInterpolator interpolator,
                            final double[] y, final double[] yDot, final double tEnd)
    throws MaxCountExceededException, DimensionMismatchException, NoBracketingException {

        double previousT = interpolator.getGlobalPreviousTime();
        final double currentT = interpolator.getGlobalCurrentTime();
        double t = previousT;

        if (!statesInitialized) {
            for (EventState state : eventsStates) {
                state.reinitializeBegin(interpolator);
            }
            statesInitialized = true;
        }

        boolean forward = interpolator.isForward();
        int orderingSign = forward ? 1 : -1;
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
            EventState currentEvent = occurringEvents.first();
            occurringEvents.remove(currentEvent);

            double eventT = currentEvent.getEventTime();
            interpolator.setSoftPreviousTime(previousT);
            interpolator.setSoftCurrentTime(eventT);
            interpolator.setInterpolatedTime(eventT);
            double[] eventY = interpolator.getInterpolatedState().clone();

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

            previousT = eventT; // Update previousT for every handling event correctly
            interpolator.setSoftPreviousTime(eventT);
            interpolator.setSoftCurrentTime(currentT);

            for (EventState state : eventsStates) {
                if (state.evaluateStep(interpolator) && !occurringEvents.contains(state)) {
                    occurringEvents.add(state);
                }
            }
        }

        interpolator.setInterpolatedTime(currentT);
        double[] currentY = interpolator.getInterpolatedState();
        for (EventState state : eventsStates) {
            state.stepAccepted(currentT, currentY);
            isLastStep = isLastStep || state.stop();
        }

        for (StepHandler handler : stepHandlers) {
            handler.handleStep(interpolator, isLastStep);
        }

        isLastStep = isLastStep || Math.abs(currentT - tEnd) <= 1e-9;

        return currentT;
    }
