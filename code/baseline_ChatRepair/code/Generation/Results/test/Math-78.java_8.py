public boolean evaluateStep(final StepInterpolator interpolator)
    throws DerivativeException, EventException, ConvergenceException {

    try {
        forward = interpolator.isForward();
        final double t1 = interpolator.getCurrentTime();
        final int n = Math.max(1, (int) Math.ceil(Math.abs(t1 - t0) / maxCheckInterval));
        final double h = (t1 - t0) / n;

        double ta = t0;
        double ga = g0;
        double tb = ta + (forward ? convergence : -convergence);
        boolean signChangeExists = ga * g0 < 0; // Initial assumption

        for (int i = 0; i < n; ++i) {
            tb += h;
            interpolator.setInterpolatedTime(tb);
            final double gb = handler.g(tb, interpolator.getInterpolatedState());

            if (g0Positive ^ (gb >= 0)) {
                if (signChangeExists || (ga * gb < 0)) {
                    increasing = gb >= ga;

                    final BrentSolver solver = new BrentSolver(convergence);
                    final UnivariateRealFunction f = t -> {
                        interpolator.setInterpolatedTime(t);
                        return handler.g(t, interpolator.getInterpolatedState());
                    };

                    try {
                        final double root = solver.solve(maxIterationCount, f, ta, tb);

                        if ((Math.abs(root - ta) <= convergence) && (Math.abs(root - previousEventTime) <= convergence)) {
                            ta = tb;
                            ga = gb;
                        } else {
                            pendingEventTime = root;
                            pendingEvent = true;
                            return true;
                        }
                    } catch (NoBracketingException e) {
                        // Log or handle the case where no sign change is bracketed
                    }
                }
            }

            ta = tb;
            ga = gb;
            signChangeExists = (ga * gb < 0);
        }
        pendingEvent = false;
        pendingEventTime = Double.NaN;
        return false;

    } catch (FunctionEvaluationException e) {
        // Re-throw or handle derivative and event exceptions similarly
        final Throwable cause = e.getCause();
        if (cause instanceof DerivativeException) {
            throw (DerivativeException) cause;
        } else if (cause instanceof EventException) {
            throw (EventException) cause;
        }
        throw new EventException(e);
    }
}
