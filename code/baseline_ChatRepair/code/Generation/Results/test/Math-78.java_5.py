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
        double gb = ga; // Initialize to prevent scope issues

        for (int i = 0; i < n; ++i) {
            interpolator.setInterpolatedTime(tb);
            gb = handler.g(tb, interpolator.getInterpolatedState());

            if (g0Positive ^ (gb >= 0)) {
                // Event detected in bracket [ta, tb]
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

                final double root;
                if (ga * gb > 0) {
                    tb += h;
                    continue; // Skip solving if no bracketing
                } else {
                    root = solver.solve(f, ta, tb);
                }

                handleEventDetection(root, t1, ga, gb, ta, tb);

            } else {
                // Continue to next bracket
                ta = tb;
                ga = gb;
                tb = ta + h;
            }
        }

        pendingEvent = false;
        pendingEventTime = Double.NaN;
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

private void handleEventDetection(double root, double t1, double ga, double gb, double ta, double tb) {
    if ((Math.abs(root - ta) <= convergence) &&
         (Math.abs(root - previousEventTime) <= convergence)) {
        ta = tb;
        ga = gb;
    } else if (Double.isNaN(previousEventTime) ||
               (Math.abs(previousEventTime - root) > convergence)) {
        pendingEventTime = root;
        if (pendingEvent && (Math.abs(t1 - pendingEventTime) <= convergence)) {
            return false;  // Event detected and handled properly
        }
        pendingEvent = true;
        return true;
    }
}
