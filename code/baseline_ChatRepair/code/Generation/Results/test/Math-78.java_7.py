public boolean evaluateStep(final StepInterpolator interpolator)
    throws DerivativeException, EventException, ConvergenceException {

    try {

        forward = interpolator.isForward();
        final double t1 = interpolator.getCurrentTime();
        final int    n  = Math.max(1, (int) Math.ceil(Math.abs(t1 - t0) / maxCheckInterval));
        final double h  = (t1 - t0) / n;

        double ta = t0;
        double ga = g0;
        double tb = t0 + (interpolator.isForward() ? convergence : -convergence);
        
        for (int i = 0; i < n; ++i) {
            // Evaluate handler value at the end of the substep
            tb += h;
            interpolator.setInterpolatedTime(tb);
            final double gb = handler.g(tb, interpolator.getInterpolatedState());

            // Check for sign change indicating an event
            if (ga * gb < 0) {
                // there is a sign change: proceed with root finding
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
                // Ensure that ta and tb are ordered correctly for the solver
                final double root = ta <= tb ? solver.solve(f, ta, tb) : solver.solve(f, tb, ta);

                // Process the event
                if ((Math.abs(root - ta) > convergence) && (Math.abs(root - previousEventTime) > convergence)) {
                    pendingEventTime = root;
                    pendingEvent = true;
                    return true;
                }
            }

            // Update the fields
            ta = tb;
            ga = gb;
        }

        // No event detected in this step
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
