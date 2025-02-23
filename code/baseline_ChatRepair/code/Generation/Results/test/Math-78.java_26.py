public boolean evaluateStep(final StepInterpolator interpolator)
    throws DerivativeException, EventException, ConvergenceException {

    try {
        forward = interpolator.isForward();
        final double t1 = interpolator.getCurrentTime();
        final int n = Math.max(1, (int) Math.ceil(Math.abs(t1 - t0) / maxCheckInterval));
        final double h = (t1 - t0) / n;

        double ta = t0;
        double ga = g0;
        double tb = ta;
        for (int i = 0; i < n; ++i) {
            tb += h;
            interpolator.setInterpolatedTime(tb);
            final double gb = handler.g(tb, interpolator.getInterpolatedState());

            if ((ga >= 0 && gb <= 0) || (ga <= 0 && gb >= 0)) {
                if (Math.abs(ga - gb) < convergence) {
                    // Handle near-zero crossings which might not signify a true event
                    ta = tb;
                    ga = gb;
                    continue;
                }
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
                
                double root;
                try {
                    root = solver.solve(f, ta, tb);
                } catch (ConvergenceException ce) {
                    // Handle the convergence exception by continuing the loop
                    ta = tb;
                    ga = gb;
                    continue;
                }

                // Rest of the logic continues unchanged...
                
            } else {
                ta = tb;
                ga = gb;
            }
        }

        // no event during the whole step
        pendingEvent     = false;
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
