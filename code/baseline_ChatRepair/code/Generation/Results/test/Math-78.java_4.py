public boolean evaluateStep(final StepInterpolator interpolator)
    throws DerivativeException, EventException, ConvergenceException {
    
    try {
        
        forward = interpolator.isForward();
        final double t1 = interpolator.getCurrentTime();
        final int n = Math.max(1, (int) Math.ceil(Math.abs(t1 - t0) / maxCheckInterval));
        final double h = (t1 - t0) / n;

        double ta = t0;
        double ga = g0;
        double tb;
        for (int i = 0; i < n; ++i) {
            tb = t0 + h * (i + 1); // safely increment each substep
            interpolator.setInterpolatedTime(tb);
            final double gb = handler.g(tb, interpolator.getInterpolatedState());

            // check if there is a zero crossing
            if (g0Positive ^ (gb >= 0)) {
                // Find the root using the Brent Solver within the bracket [ta, tb]
                final UnivariateRealFunction function = new UnivariateRealFunction() {
                    public double value(final double t) throws FunctionEvaluationException {
                        try {
                            interpolator.setInterpolatedTime(t);
                            return handler.g(t, interpolator.getInterpolatedState());
                        } catch (DerivativeException | EventException e) {
                            throw new FunctionEvaluationException(e, t);
                        }
                    }
                };

                final BrentSolver solver = new BrentSolver();
                solver.setAbsoluteAccuracy(convergence);
                solver.setMaximalIterationCount(maxIterationCount);

                if (gb >= ga == forward) { // Check that we have the correct order for root finding
                    double root = solver.solve(function, ta, tb);
                    double threshold = Math.max(convergence, 1e-14);  // Ensure non-zero width for convergence
                    if (Math.abs(root - ta) > threshold && Math.abs(root - tb) > threshold) {
                        pendingEventTime = root;
                        pendingEvent = true;
                        return true;
                    }
                }

                // Update ga for the next segment
                ga = gb;
                ta = tb;
            } else {
                // Continue to next interval
                ta = tb;
                ga = gb;
            }
        }

        // No event detected
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
