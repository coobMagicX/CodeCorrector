public boolean evaluateStep(final StepInterpolator interpolator)
        throws DerivativeException, EventException, ConvergenceException {

    try {
        // Retrieve the current time from the interpolator
        final double t1 = interpolator.getCurrentTime();
        
        // Calculate the number of substeps based on maxCheckInterval to avoid very small steps
        int n = (int) Math.max(1, Math.ceil(Math.abs(t1 - t0) / maxCheckInterval));
        final double h = (t1 - t0) / n;

        for (int i = 0; i < n; ++i) {
            // Calculate the current substep's end time
            double tb = t0 + h * (i + 1);
            
            // Update interpolator to the new value and evaluate handler at this point
            interpolator.setInterpolatedTime(tb);
            final double gb = handler.g(tb, interpolator.getCurrentState());
            
            // Check for event occurrence by comparing signs of consecutive substeps
            if ((g0Positive != (gb >= 0)) && !Double.isNaN(gb)) {
                // An event has been detected. Handle the corner case where two events are too close.
                double root = findRoot(interpolator, g0Positive, gb);
                
                if (!Double.isNaN(root)) {
                    stepAccepted(root, interpolator.getCurrentState());
                    return false; // Return false to indicate an event occurred
                }
            }
            
            // Update variables for next iteration
            g0Positive = (gb >= 0);
        }

        // No events were detected during the entire step
        pendingEvent     = false;
        pendingEventTime = Double.NaN;
        return false;

    } catch (FunctionEvaluationException e) {
        // Handle specific types of exceptions or rethrow them with more detail if needed
        throw handleSpecificException(e);
    }
}

private double findRoot(StepInterpolator interpolator, boolean g0Positive, double gb) {
    // Implement a root-finding algorithm (e.g., using a Brent solver)
    // This is just a placeholder for the actual implementation
    return 0.0;
}

private void stepAccepted(double t, double[] y) throws EventException {
    // Handle step acceptance logic as per the previous code
}