public boolean evaluateStep(final StepInterpolator interpolator)
    throws DerivativeException, EventException, ConvergenceException {

    try {

        forward = interpolator.isForward();
        final double t1 = interpolator.getCurrentTime();
        final int n = Math.max(1, (int) Math.ceil(Math.abs(t1 - t0) / maxCheckInterval));
        double h = (t1 - t0) / n;

        double ta = t0;
        double ga = g0;
        double tb = ta + h;
        boolean signChangeDetected = false;

        for (int i = 0; i < n; ++i) {
            interpolator.setInterpolatedTime(tb);
            final double gb = handler.g(tb, interpolator.getInterpolatedState());

            if (g0Positive ^ (gb >= 0)) { // Check opposing signs
                signChangeDetected = true;
                increasing = gb >= ga;

                try {
                    final BrentSolver solver = new BrentSolver();
                    solver.setAbsoluteAccuracy(convergence);
                    solver.setMaximalIterationCount(maxIterationCount);
                    double root = solver.solve(new UnivariateRealFunction() {
                        public double value(final double t) throws FunctionEvaluationException {
                            try {
                                interpolator.setInterpolatedTime(t);
                                return handler.g(t, interpolator.getInterpolatedState());
                            } catch (DerivativeException | EventException e) {
                                throw new FunctionEvaluationException(e, t);
                            }
                        }
                    }, ta, tb);

                    updateEventState(root); 
                    return findAndHandleEvent(ta, tb, root);
                } catch (ConvergenceException ex) {
                    // Handle convergence failure (e.g., no sign change)
                    System.out.println("Failed to converge: " + ex.getMessage());
                    return false;
                }
            } else {
                ta = tb;
                ga = gb;
                tb += h;
            }
        }
        if (!signChangeDetected) {
            // Handle case when no sign changes were detected throughout the step
            System.out.println("No sign changes were detected in the whole interval.");
        }
        pendingEvent = false;
        pendingEventTime = Double.NaN;
        return false;

    } catch (FunctionEvaluationException e) {
        throw handleEvaluationException(e);
    }
}
