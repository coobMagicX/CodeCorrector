public boolean evaluateStep(final StepInterpolator interpolator)
    throws DerivativeException, EventException, ConvergenceException {
    try {
        forward = interpolator.isForward();
        final double t1 = interpolator.getCurrentTime();
        final int n = Math.max(1, (int) Math.ceil(Math.abs(t1 - t0) / maxCheckInterval));
        final double h = (t1 - t0) / n;

        double ta = t0;
        double ga = g0;
        double tb = t0 + (interpolator.isForward() ? convergence : -convergence);

        for (int i = 0; i < n; ++i) {
            tb = (i == n-1) ? t1 : ta + h; // Adjust tb on the last iteration to match exactly t1
            interpolator.setInterpolatedTime(tb);
            final double gb = handler.g(tb, interpolator.getInterpolatedState());

            if (g0Positive ^ (gb >= 0)) { // Check for sign change
                increasing = gb >= ga;

                // Ensure that endpoints have values of different signs for root finding
                if (gb * ga > 0) {
                    ga = -ga; // Flip ga to ensure different signs
                }

                final UnivariateRealFunction f = new UnivariateRealFunction() {
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

                final double root = solver.solve(f, ta, tb);
                if ((Math.abs(root - ta) <= convergence) && (Math.abs(root - previousEventTime) <= convergence)) {
                    ta = tb;
                    ga = gb;
                    continue; // Skip since this event was too close to the last event or start
                }

                pendingEventTime = root;
                pendingEvent = true;
                return true;
            } else {
                ta = tb;
                ga = gb;
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
