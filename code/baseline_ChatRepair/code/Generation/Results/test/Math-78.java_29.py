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

            // increment tb for the next step
            tb = ta + h;

            interpolator.setInterpolatedTime(tb);
            final double gb = handler.g(tb, interpolator.getInterpolatedState());

            // consider adding a small epsilon to correctly detect sign changes
            if (g0Positive ^ (gb >= 0) && Math.abs(gb - ga) > EPSILON) {
                // a potential root must be bracketed if there was a sign change between ga and gb
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
                double root = solver.solve(f, ta, tb);

                // Check if the root is within convergence range 
                if ((Math.abs(root - ta) > convergence) &&
                    (Math.abs(root - previousEventTime) > convergence)) {
                    pendingEventTime = root;
                    pendingEvent = true;
                    return true;
                } else {
                    ta = tb; 
                    ga = gb;
                }
            } else {
                // continue to the next step
                ta = tb;
                ga = gb;
            }
        }

        // no event detected throughout the loop
        pendingEvent = false;
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
