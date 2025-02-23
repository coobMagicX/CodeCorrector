public boolean evaluateStep(final StepInterpolator interpolator)
    throws DerivativeException, EventException, ConvergenceException {

    try {
        forward = interpolator.isForward();
        final double t1 = interpolator.getCurrentTime();
        final int n = Math.max(1, (int) Math.ceil(Math.abs(t1 - t0) / maxCheckInterval));
        final double h = (t1 - t0) / n;

        double ta = t0, ga = g0;
        double tb = t0 + ((forward ? 1 : -1) * convergence);

        for (int i = 0; i < n; ++i) {
            tb += h;
            interpolator.setInterpolatedTime(tb);
            final double gb = handler.g(tb, interpolator.getInterpolatedState());

            if (g0Positive ^ (gb >= 0)) {
                increasing = gb >= ga;
                
                if (Math.signum(ga) == Math.signum(gb)) {
                    ga -= Math.ulp(ga); // Adjust ga slightly to handle the endpoint sign issue
                }

                final BrentSolver solver = new BrentSolver();
                solver.setAbsoluteAccuracy(convergence);
                solver.setMaximalIterationCount(maxIterationCount);

                try {
                    final double root = solver.solve(ta, tb, ga, gb);
                    if (!Double.isNaN(root)) {
                        pendingEventTime = root;
                        pendingEvent = true;
                        return true;
                    }
                } catch (ConvergenceException e) {
                    // If there's a convergence exception here, it may be wise to react or adjust settings.
                }
            } 

            // Update current step
            ta = tb;
            ga = gb;
        }

        pendingEvent = false;
        pendingEventTime = Double.NaN;
        return false;

    } catch (Exception e) {
        // Various exceptions wrapped into a generic block for simplicity
        throw new EventException(e);
    }
}
