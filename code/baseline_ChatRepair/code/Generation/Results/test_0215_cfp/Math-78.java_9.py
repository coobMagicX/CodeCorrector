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
            // Move tb forward by step size h
            if (i != 0) {
                tb += h;
            }
            interpolator.setInterpolatedTime(tb);
            final double gb = handler.g(tb, interpolator.getInterpolatedState());

            if ((ga >= 0 && gb < 0) || (ga < 0 && gb >= 0)) {
                // Sign change detected, meaning a potential event.
                increasing = gb >= ga;

                final BrentSolver solver = new BrentSolver();
                solver.setAbsoluteAccuracy(convergence);
                solver.setMaximalIterationCount(maxIterationCount);

                double lower = ta, upper = tb;
                if (!increasing) {
                    // Swap bounds if the function is decreasing in this interval
                    lower = tb;
                    upper = ta;
                }

                final double root = solver.solve(f, lower, upper);
                if (Math.abs(root - ta) <= convergence && Math.abs(root - previousEventTime) <= convergence) {
                    ta = tb;
                    ga = gb;
                    continue;
                }
                if (Double.isNaN(previousEventTime) || Math.abs(previousEventTime - root) > convergence) {
                    pendingEventTime = root;
                    pendingEvent = true;
                    return true;
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
