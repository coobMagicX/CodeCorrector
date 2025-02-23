public boolean evaluateStep(final StepInterpolator interpolator)
    throws DerivativeException, EventException, ConvergenceException {
    
    try {
        forward = interpolator.isForward();
        final double t1 = interpolator.getCurrentTime();
        final int n = Math.max(1, (int) Math.ceil(Math.abs(t1 - t0) / maxCheckInterval));
        final double h = (t1 - t0) / n;

        double ta = t0;
        double ga = g0;
        double tb, gb;

        for (int i = 0; i < n; ++i) {
            tb = t0 + (i + 1) * h;
            interpolator.setInterpolatedTime(tb);
            gb = handler.g(tb, interpolator.getInterpolatedState());

            if (g0Positive ^ (gb >= 0)) {
                if ((gb >= 0) == (ga >= 0)) {
                    // Refine ga to ensure it has an opposite sign to gb
                    ga = handler.g(ta - Math.signum(h) * convergence, interpolator.getInterpolatedState());
                }

                increasing = gb >= ga;

                try {
                    // Check the bracketing, the signs at ta and tb must be different
                    if ((gb >= 0) != (ga >= 0)) {
                        final BrentSolver solver = new BrentSolver();
                        solver.setAbsoluteAccuracy(convergence);
                        solver.setMaximalIterationCount(maxIterationCount);
                        final double root = solver.solve(ta, tb, new UnivariateRealFunction() {
                            public double value(final double t) throws FunctionEvaluationException {
                                try {
                                    interpolator.setInterpolatedTime(t);
                                    return handler.g(t, interpolator.getInterpolatedState());
                                } catch (DerivativeException | EventException e) {
                                    throw new FunctionEvaluationException(e, t);
                                }
                            }
                        });

                        if (Math.abs(root - tb) > convergence && 
                        (Double.isNaN(previousEventTime) || Math.abs(previousEventTime - root) > convergence)) {
                            pendingEventTime = root;
                            pendingEvent = true;
                            return true;
                        }
                    }
                } catch (FunctionEvaluationException e) {
                    // Handle the exception possibly by deciding the best course of action
                }
            }

            ta = tb;
            ga = gb;
        }

        pendingEvent = false;
        pendingEventTime = Double.NaN;
        return false;

    } catch (FunctionEvaluationException e) {
        final Throwable cause = e.getCause();
        if ((cause != null) && (cause instanceof DerivativeException)) {
            throw (DerivativeException) cause;
        }
        throw new EventException(e);
    }
}
