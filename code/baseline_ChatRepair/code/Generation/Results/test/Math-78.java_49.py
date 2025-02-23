public boolean evaluateStep(final StepInterpolator interpolator) throws DerivativeException, EventException, ConvergenceException {
    try {
        forward = interpolator.isForward();
        final double t1 = interpolator.getCurrentTime();
        final int n = Math.max(1, (int) Math.ceil(Math.abs(t1 - t0) / maxCheckInterval));
        final double h = (t1 - t0) / n;

        double ta = t0;
        double ga = g0;
        double tb = t0 + (interpolator.isForward() ? convergence : -convergence);
        for (int i = 0; i < n; ++i) {
            tb += h;
            interpolator.setInterpolatedTime(tb);
            final double gb = handler.g(tb, interpolator.getInterpolatedState());

            if (g0Positive ^ (gb >= 0)) { 
                increasing = gb >= ga;

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
                
                // Ensure that ga and gb have different signs to provide valid bracketing
                if (Double.doubleToLongBits(ga * gb) < 0) { 
                    final double root = (ta <= tb) ? solver.solve(f, ta, tb, ga, gb) : solver.solve(f, tb, ta, gb, ga);

                    if ((Math.abs(root - ta) <= convergence) &&
                        (Math.abs(root - previousEventTime) <= convergence)) {
                        ta = tb;
                        ga = gb;
                        continue;
                    } else if (Double.isNaN(previousEventTime) || (Math.abs(previousEventTime - root) > convergence)) {
                        pendingEventTime = root;
                        pendingEvent = true;
                        return true;
                    }
                    ta = tb;
                    ga = gb;
                } else {
                    // Adjust end points or handle missing event
                    ta = tb;
                    ga = gb;
                    continue;
                }
            } else {
                ta = tb;
                ga = gb;
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
