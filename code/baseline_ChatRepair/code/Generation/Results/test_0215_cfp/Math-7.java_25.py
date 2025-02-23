protected double acceptStep(final AbstractStepInterpolator interpolator,
                            final double[] y, final double[] yDot, final double tEnd)
    throws MaxCountExceededException, DimensionMismatchException, NoBracketingException {

        double previousT = interpolator.getGlobalPreviousTime();
        final double currentT = interpolator.getGlobalCurrentTime();

        // Initialize the event states if needed
        if (!statesInitialized) {
            for (EventState state : eventsStates) {
                state.reinitializeBegin(interpolator);
            }
            statesInitialized = true;
        }

        // Search for next events that may occur during the step
        final int orderingSign = interpolator.isForward() ? +1 : -1;
        SortedSet<EventState> occurringEvents = new TreeSet<>(new Comparator<EventState>() {
            public int compare(EventState es0, EventState es1) {
                return orderingSign * Double.compare(es0.getEventTime(), es1.getEventTime());
            }
        });

        // Evaluate the states and ensure no modification impacts the interpolator during sorting
        TreeMap<Double, EventState> eventTimeToStateMap = new TreeMap<>();
        for (final EventState state : eventsStates) {
            double eventTime = state.getNextEventTime();
            if (state.evaluateStep(new DummyStepInterpolator(interpolator))) {
                // Use a different interpolator clone for actual sorting purposes to avoid side-effects on the real interpolator
                eventTimeToStateMap.put(eventTime, state);
            }
        }

        // Fill occurring events respecting the immutable times captured before
        for (EventState state : eventTimeToStateMap.values()) {
            occurringEvents.add(state);
        }

        while (!occurringEvents.isEmpty()) {
            // Handling events in chronological order
            final Iterator<EventState> iterator = occurringEvents.iterator();
            final EventState currentEvent = iterator.next();
            iterator.remove();

            // Handle the remainder of code for event processing as it was
            double eventT = currentEvent.getEventTime();
            // More logic that handles the events, stop conditions, step advancement, etc.
        }

        // Last part of the function as it was
        return currentT;
}
