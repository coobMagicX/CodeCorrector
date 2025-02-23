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
            tb += h;
            interpolator.setInterpolatedTime(tb);
            final double gb = handler.g(tb, interpolator.getInterpolatedState());

            if (Double.isNaN(gb) || Double.isNaN(ga)) {
                throw new EventException("Evaluation of g function returned NaN");
            }

            if (g0Positive ^ (gb >= 0)) {
                increasing = gb >= ga;

                final UnivariateRealFunction f = t -> {
                    try {
                        interpolator.setInterpolatedTime(t);
                        return handler.g(t, interpolator.getInterpolatedState());
                    } catch (DerivativeException | EventException e) {
                        throw new FunctionEvaluationException(e, t);
                    }
                };

                final BrentSolver solver = new BrentSolver();
                solver.setAbsoluteAccuracy(convergence);
                solver.setMaximalIterationCount(maxIterationCount);

                // Only solve if there's a sign change, ensuring solver's preconditions are met:
                if (ga * gb <= 0) {
                    final double root = solver.solve(f, ta, tb);
                    
                    if ((Math.abs(root - ta) <= convergence) &&
                        (Math.abs(root - previousEventTime) <= convergence)) {
                        ta = tb;
                        ga = gb;
                    } else if (Double.isNaN(previousEventTime) ||
                                (Math.abs(previousEventTime - root) > convergence)) {
                        pendingEventTime = root;
                        pendingEvent = true;
                        return true;
                    }

                } else {
                    // Continues as no sign change, no root in this region
                    ta = tb;
                    ga = gb;
                }
            } else {
                ta = tb;
                ga = gb;
            }
        }

        pendingEvent     = false;
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
