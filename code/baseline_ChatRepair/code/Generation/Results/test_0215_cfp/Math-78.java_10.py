public boolean evaluateStep(final StepInterpolator interpolator)
    throws DerivativeException, EventException, ConvergenceException {
    try {
        forward = interpolator.isForward();
        final double t1 = interpolator.getCurrentTime();
        final int n = Math.max(1, (int) Math.ceil(Math.abs(t1 - t0) / maxCheckInterval));
        final double h = (t1 - t0) / n;

        double ta = t0;
        double ga = g0;
        for (int i = 0; i < n; ++i) {
            double tb = ta + h;
            interpolator.setInterpolatedTime(tb);
            double gb = handler.g(tb, interpolator.getInterpolatedState());

            if (g0Positive ^ (gb >= 0)) {  // Check sign change
                increasing = gb >= ga;

                // Redefine endpoints if they do not bracket the root
                if (!(ga * gb < 0)) {
                    // Attempt to find new tb (tb_new) such that ga and g(tb_new) bracket a root
                    double tmp = ga;
                    double step = h;
                    for (int j = 0; !((tmp * gb) < 0) && j < n; ++j) {
                        step /= 2;
                        if (g0Positive == (tmp >= 0)) {
                            tb += step; // Move tb closer to root
                        } else {
                            tb -= step; // Move tb closer to root
                        }
                        tmp = handler.g(tb, interpolator.getInterpolatedState());
                    }
                    gb = tmp;
                    if (!(ga * gb < 0)) {
                        // If still not bracketing, throw an exception or handle this case separately
                        continue;  // Or handle appropriately
                    }
                }

                // Now solve with the solver since ga and gb bracket the root
                final BrentSolver solver = new BrentSolver();
                solver.setAbsoluteAccuracy(convergence);
                solver.setMaximalIterationCount(maxIterationCount);
                double root = (ta < tb) ? solver.solve(f, ta, tb) : solver.solve(f, tb, ta);
                // remaining code unchanged...

            } else {
                ta = tb;
                ga = gb;
            }
        }
        // remaining code unchanged...
    } catch (FunctionEvaluationException e) {
        // exception handling unchanged...
    }
}
