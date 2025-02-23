public boolean evaluateStep(final StepInterpolator interpolator)
    throws DerivativeException, EventException, ConvergenceException {

    try {
        forward = interpolator.isForward();
        double t1 = interpolator.getCurrentTime();
        int n = Math.max(1, (int) Math.ceil(Math.abs(t1 - t0) / maxCheckInterval));
        double h = (t1 - t0) / n;

        double tPrevious = t0;
        double gPrevious = g0;
        double tCurrent = t0 + (interpolator.isForward() ? convergence : -convergence);

        for (int i = 0; i < n; ++i) {
            tCurrent += h;
            interpolator.setInterpolatedTime(tCurrent);
            double gCurrent = handler.g(tCurrent, interpolator.getInterpolatedState());

            // Check for sign change
            if (g0Positive ^ (gCurrent >= 0)) {
                increasing = gCurrent >= gPrevious;
                final UnivariateRealFunction f = s -> {
                    interpolator.setInterpolatedTime(s);
                    return handler.g(s, interpolator.getInterpolatedState());
                };

                final BrentSolver solver = new BrentSolver();
                solver.setAbsoluteAccuracy(convergence);
                solver.setMaximalIterationCount(maxIterationCount);

                // Ensuring that the endpoints bracket a root
                if (gPrevious * gCurrent < 0) {  // Check if different signs
                    double root = solver.solve(f, tPrevious, tCurrent);
                    handleRoot(root, t1);
                } else {
                    // Adjust interval and recheck for proper bracketing
                    tCurrent += convergence;  // Adjust tCurrent
                    interpolator.setInterpolatedTime(tCurrent);
                    double adjustedGCurrent = handler.g(tCurrent, interpolator.getInterpolatedState());
                    if (gPrevious * adjustedGCurrent < 0) {
                        double root = solver.solve(f, tPrevious, tCurrent);
                        handleRoot(root, t1);
                    } else {
                        continue;  // Skip to the next iteration
                    }
                }
            } else {
                tPrevious = tCurrent;
                gPrevious = gCurrent;
            }
        }

        pendingEvent = false;
        pendingEventTime = Double.NaN;
        return false;
    } catch (FunctionEvaluationException e) {
        final Throwable cause = e.getCause();
        if (cause instanceof DerivativeException) {
            throw (DerivativeException) cause;
        } else if (cause instanceof EventException) {
            throw (EventException) cause;
        }
        throw new EventException(e);
    }
}

private void handleRoot(double root, double t1) throws EventException {
    if ((Math.abs(root - tPrevious) <= convergence) &&
        (Math.abs(root - previousEventTime) <= convergence)) {
        // Ignore close or duplicate events
        tPrevious = tCurrent;
        gPrevious = gCurrent;
    } else if (Double.isNaN(previousEventTime) ||
               (Math.abs(previousEventTime - root) > convergence)) {
        pendingEventTime = root;
        if (pendingEvent && (Math.abs(t1 - pendingEventTime) <= convergence)) {
            return false;
        }
        pendingEvent = true;
        return true;
    }
}
