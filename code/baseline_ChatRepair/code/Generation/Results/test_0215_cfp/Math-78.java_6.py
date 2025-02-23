public boolean evaluateStep(final StepInterpolator interpolator)
    throws DerivativeException, EventException, ConvergenceException {

    try {

        forward = interpolator.isForward();
        final double t1 = interpolator.getCurrentTime();
        final int    n  = Math.max(1, (int) Math.ceil(Math.abs(t1 - t0) / maxCheckInterval));
        final double h  = (t1 - t0) / n;

        double ta = t0;
        double ga = g0;
        for (int i = 0; i < n; ++i) {

            tb = ta + h;
            interpolator.setInterpolatedTime(tb);
            final double gb = handler.g(tb, interpolator.getInterpolatedState());

            if (g0Positive ^ (gb >= 0)) {  // Ensuring a change in sign occurred
               
                if (tb == t0 + h && ga * gb > 0) {
                    // Adjust tb slightly to ensure sign change around the interval.
                    tb = (gb > 0) ? tb - convergence : tb + convergence;
                    interpolator.setInterpolatedTime(tb);
                    gb = handler.g(tb, interpolator.getInterpolatedState());
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
                if (ga * gb < 0) { // Execute this block only if signs are different
                    final double root = solver.solve(f, ta, tb);
                    if (isEventCloseEnough(root)) {
                        // Handle event if root is close enough to previous or step border.
                        handleDetectedEvent(root, ta, tb);
                        return true;
                    }
                }

                ta = tb;
                ga = gb;
            }
        }

        // no event during the whole step
        pendingEvent     = false;
        pendingEventTime = Double.NaN;
        return false;

    } catch (FunctionEvaluationException e) {
        propagateDerivativeOrEventException(e);
    }
}

// These helper methods handle specific cases that you may wish to customize.
private void propagateDerivativeOrEventException(FunctionEvaluationException e) throws DerivativeException, EventException {
    final Throwable cause = e.getCause();
    if (cause instanceof DerivativeException) {
        throw (DerivativeException) cause;
    } else if (cause instanceof EventException) {
        throw (EventException) cause;
    }
    throw new EventException(e);
}

private boolean isEventCloseEnough(double eventTime) {
    return (Math.abs(eventTime - previousEventTime) > convergence) &&
           (Math.abs(eventTime - getCurrentTime()) <= convergence);
}

private void handleDetectedEvent(double eventTime, double ta, double tb) {
    pendingEventTime = eventTime;
    pendingEvent = true;
}
