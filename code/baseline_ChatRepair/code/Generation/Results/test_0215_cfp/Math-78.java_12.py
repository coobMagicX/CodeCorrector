public boolean evaluateStep(final StepInterpolator interpolator)
    throws DerivativeException, EventException, ConvergenceException {

    try {

        forward = interpolator.isForward();
        final double t1 = interpolator.getCurrentTime();
        final int n = Math.max(1, (int) Math.ceil(Math.abs(t1 - t0) / maxCheckInterval));
        final double h = (t1 - t0) / n;

        double ta = t0;
        double ga = g0;
        double tb = ta + (interpolator.isForward() ? convergence : -convergence);
        for (int i = 0; i < n; ++i) {
            tb += h;

            interpolator.setInterpolatedTime(tb);
            final double gb = handler.g(tb, interpolator.getInterpolatedState());

            if (g0Positive ^ (gb >= 0)) {
                increasing = gb >= ga;

                if ((ga <= 0 && gb >= 0) || (ga >= 0 && gb <= 0)) {
                    // only solve if there is an actual sign change
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

                    if (!Double.isNaN(root) && (Math.abs(root - ta) > convergence) && (Math.abs(root - previousEventTime) > convergence)) {
                        pendingEventTime = root;
                        pendingEvent = true;
                        return true;
                    }
                } 

                // modify ta and ga
                ta = tb;
                ga = gb;
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
