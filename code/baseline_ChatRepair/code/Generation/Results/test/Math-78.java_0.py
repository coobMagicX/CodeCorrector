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
            final double gb = handler.g(tb, interpolator.getInterpolatedState());

            if ((ga >= 0 && gb < 0) || (ga < 0 && gb >= 0)) {
                // Sign change detected
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
                try {
                    final double root = solver.solve(f, ta, tb);
                    if ((Math.abs(root - ta) <= convergence) &&
                            (Math.abs(root - previousEventTime) <= convergence)) {
                        // Ignore close or repeated events
                        ta = tb;
                        ga = gb;
                        continue;
                    }

                    pendingEventTime = root;
                    if (pendingEvent && (Math.abs(t1 - pendingEventTime) <= convergence)) {
                        return false;
                    }
                    pendingEvent = true;
                    return true;
                } catch (FunctionEvaluationException e) {
                    // Handle case where no bracketing could be found
                    ta = tb;
                    ga = gb;
                    continue;
                }
            }

            // Update for next iteration
            ta = tb;
            ga = gb;
        }

        pendingEvent = false;
        pendingEventTime = Double.NaN;
        return false;
    } catch (FunctionEvaluationException e) {
        throw new EventException(e);
    }
}
