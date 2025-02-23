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

            if (g0Positive ^ (gb >= 0)) {
                if (ga * gb > 0) {   // explicitly check if ga and gb have the same signs
                    // Adjust tb slightly to attempt to find a bracketing interval:
                    tb -= h / 1000.0;
                    gb = handler.g(tb, interpolator.getInterpolatedState());
                }

                if (ga * gb <= 0) {  // Now check if they have opposite signs
                    increasing = gb >= ga;
                    final BrentSolver solver = new BrentSolver();
                    solver.setAbsoluteAccuracy(convergence);
                    solver.setMaximalIterationCount(maxIterationCount);
                    double root;
                    try {
                        root = solver.solve(new UnivariateRealFunction() {
                            public double value(final double t) throws FunctionEvaluationException {
                                try {
                                    interpolator.setInterpolatedTime(t);
                                    return handler.g(t, interpolator.getInterpolatedState());
                                } catch (DerivativeException | EventException e) {
                                    throw new FunctionEvaluationException(e, t);
                                }
                            }
                        }, ta, tb);
                    } catch (FunctionEvaluationException e) {
                        continue;  // Skip to next interval in case of a solver failure
                    }

                    proceedAfterEventHandling(root, tb, ta, ga, gb, t1);
                    return true;  // Event detected and handled
                }
            }

            ta = tb;
            ga = gb;
        }

        pendingEvent = false;
        pendingEventTime = Double.NaN;
        return false;
    } catch (FunctionEvaluationException e) {
        throw translateToAppropriateException(e);
    }
}
