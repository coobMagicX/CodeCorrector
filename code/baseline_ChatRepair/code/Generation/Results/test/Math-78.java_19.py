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
        double gb = ga;

        for (int i = 0; i < n; ++i) {

            tb += h;
            interpolator.setInterpolatedTime(tb);
            gb = handler.g(tb, interpolator.getInterpolatedState());

            // Check for zero crossings, sign change from ga to gb
            if (ga * gb <= 0) {
                if (ga == 0 || gb == 0) {
                    // Handle the case where ga or gb is exactly zero.
                    pendingEventTime = (ga == 0) ? ta : tb;
                    pendingEvent = true;
                    return true;
                }

                increasing = gb > ga;

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
                
                if ((Math.abs(root - ta) <= convergence) &&
                     (Math.abs(root - previousEventTime) <= convergence)) {
                    ta = tb;
                    ga = gb;
                } else {
                    pendingEventTime = root;
                    pendingEvent = true;
                    return true;
                }
            } 

            ta = tb;
            ga = gb;
        }

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
