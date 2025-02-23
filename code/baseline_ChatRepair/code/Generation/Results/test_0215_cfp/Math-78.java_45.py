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

            // evaluate handler value at the beginning and end of the substep
            interpolator.setInterpolatedTime(tb + h);
            final double gb = handler.g(tb + h, interpolator.getInterpolatedState());

            // check events occurrence
            if (g0Positive ^ (gb >= 0)) {
                // there is a sign change: an event might occur during this step
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

                // Solve for root only if signs of ga and gb are different
                if (ga * gb < 0) {
                    double root = solver.solve(f, ta, tb + h);
                    if (!Double.isNaN(root)) {
                        pendingEventTime = root;
                        pendingEvent = true;
                        return true;
                    }
                }
            }

            // Update t_a and g_a for next iteration
            tb += h;
            ta = tb;
            ga = gb;
        }

        // no event during the whole step
        pendingEvent     = false;
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
