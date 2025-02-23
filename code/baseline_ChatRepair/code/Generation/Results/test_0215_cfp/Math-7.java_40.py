protected double acceptStep(final AbstractStepInterpolator interpolator,
                            final double[] y, final double[] yDot, final double tEnd)
    throws MaxCountExceededException, DimensionMismatchException, NoBracketingException {

    // Existing implementations up to the event evaluation

    final double tolerance = 1e-10;  // Tolerance for comparing event times, adjust as necessary

    SortedSet<EventState> occurringEvents = new TreeSet<EventState>(new Comparator<EventState>() {
        public int compare(EventState es0, EventState es1) {
            double diff = es0.getEventTime() - es1.getEventTime();
            if (Math.abs(diff) < tolerance) {
                return 0;  // Consider times equal if they are within tolerance
            }
            return orderingSign * (diff < 0 ? -1 : 1);
        }
    });

    // Continue with the existing logic for handling events

    return currentT;  // existing return statement
}
