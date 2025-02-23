public boolean evaluateStep(final StepInterpolator interpolator)
    throws DerivativeException, EventException, ConvergenceException {

    try {

        forward = interpolator.isForward();
        final double t1 = interpolator.getCurrentTime();
        final int n = Math.max(1, (int) Math.ceil(Math.abs(t1 - t0) / maxCheckInterval));
        final double h = (t1 - t0) / n;

        double ta = t0;
        double ga = g0;
        double tb = ta + h;
        
        for (int i = 0; i < n; ++i) {
            interpolator.setInterpolatedTime(tb);
            final double gb = handler.g(tb, interpolator.getInterpolatedState());

            // check events occurrence
            if ((ga >= 0) != (gb >= 0)) { // Ensuring checking for sign changes
                increasing = gb >= ga;

                final UnivariateRealFunction f = t -> {
                    interpolator.setInterpolatedTime(t);
                    return handler.g(t, interpolator.getInterpolatedState());
                };

                final BrentSolver solver = new BrentSolver();
                solver.setAbsoluteAccuracy(convergence);
                solver.setMaximalIterationCount(maxIterationCount);

                try {
                    final double root = (ta <= tb) ? solver.solve(f, ta, tb) : solver.solve(f, tb, ta);
                    if ((Math.abs(root - ta) <= convergence) &&
                        (Math.abs(root - previousEventTime) <= convergence)) {
                        ta = tb;
                        ga = gb;
                    } else {
                        pendingEventTime = root;
                        pendingEvent = true;
                        return true;
                    }
                } catch (FunctionEvaluationException ex) {
                    // Log or handle corner case
                }
                
            } else {
                ta = tb;
                ga = gb;
            }

            tb += h; // Increase tb by step size
            if (i == n - 2) {
                tb = t1; // Ensure the last point is exactly t1
            }
        }

        pendingEvent = false;
        pendingEventTime = Double.NaN;
        return false;

    } catch (FunctionEvaluationException e) {
        Throwable cause = e.getCause();
        if (cause instanceof DerivativeException) {
            throw (DerivativeException) cause;
        } else if (cause instanceof EventException) {
            throw (EventException) cause;
        }
        throw new EventException(e);
    }
}
