public boolean evaluateStep(final StepInterpolator interpolator)
    throws DerivativeException, EventException, ConvergenceException {

    try {

        forward = interpolator.isForward();
        final double t1 = interpolator.getCurrentTime();
        final int    n  = Math.max(1, (int) Math.ceil(Math.abs(t1 - t0) / maxCheckInterval));
        final double h  = (t1 - t0) / n;

        double ta = t0;
        double ga = g0;
        double tb = t0 + (forward ? convergence : -convergence);
        boolean signChangeDetected = false;
        for (int i = 0; i < n; ++i) {
            // update tb for each substep, considering the integration direction
            tb = ta + h;

            interpolator.setInterpolatedTime(tb);
            final double gb = handler.g(tb, interpolator.getInterpolatedState());

            // check if there is a sign change
            if (g0Positive ^ (gb >= 0)) {
                // A sign change has been detected
                signChangeDetected = true;

                increasing = gb >= ga;

                final UnivariateRealFunction f = new UnivariateRealFunction() {
                    public double value(final double t) throws FunctionEvaluationException {
                        try {
                            interpolator.setInterpolatedTime(t);
                            return handler.g(t, interpolator.getInterpolatedState());
                        } catch (DerivativeException e) {
                            throw new FunctionEvaluationException(e, t);
                        } catch (EventException e) {
                            throw new FunctionEvaluationException(e, t);
                        }
                    }
                };

                final BrentSolver solver = new BrentSolver();
                solver.setAbsoluteAccuracy(convergence);
                solver.setMaximalIterationCount(maxIterationCount);

                // Ensure that the endpoints have different signs, recompute if necessary
                if (ga * gb > 0) {
                    ga = handler.g(ta, interpolator.getInterpolatedState());
                }
                final double root = solver.solve(f, ta, tb);
                previousEventTime = root;

                // Process the detected event
                if (Math.abs(root - t1) <= convergence) {
                    pendingEvent = true;
                    pendingEventTime = root;
                    return true;
                }

                // Update ta, ga to the end of the interval after checking sign change
                ta = tb;
                ga = gb;
            } else {
                // Move to next subinterval, no sign change detected
                ta = tb;
                ga = gb;
            }
        }

        // If no sign change was detected, reset event detection.
        if (!signChangeDetected) {
            pendingEvent = false;
            pendingEventTime = Double.NaN;
        }

        return false;

    } catch (FunctionEvaluationException e) {
        final Throwable cause = e.getCause();
        if ((cause != null) && (cause instanceof DerivativeException)) {
            throw (DerivativeException) cause;
        } else if ((cause != null) && (cause instanceof EventException)) {
            throw (EventException) cause;
        }
        throw new EventException(e);
    }

}
