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
        interpolator.setInterpolatedTime(ta);
        ga = handler.g(ta, interpolator.getInterpolatedState());

        for (int i = 0; i < n; ++i) {
            tb = ta + h;
            interpolator.setInterpolatedTime(tb);
            final double gb = handler.g(tb, interpolator.getInterpolatedState());

            if (g0Positive ^ (gb >= 0)) {
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

                // Attempt to bracket the root more precisely to avoid "values at endpoints do not have different signs" error
                if (ga * gb > 0) { // If no sign change, readjust interval
                    double tAdjust = (ta + tb) / 2;
                    while (ta < tAdjust && tAdjust < tb) {
                        interpolator.setInterpolatedTime(tAdjust);
                        if (ga * handler.g(tAdjust, interpolator.getInterpolatedState()) < 0) {
                            tb = tAdjust;
                            break;
                        }
                        tAdjust = (ta + tAdjust) / 2;
                    }
                    interpolator.setInterpolatedTime(tb);
                    gb = handler.g(tb, interpolator.getInterpolatedState());
                }

                final double root = (ta <= tb) ? solver.solve(f, ta, tb) : solver.solve(f, tb, ta);

                if (Double.isNaN(previousEventTime) || (Math.abs(previousEventTime - root) > convergence)) {
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
        if (cause instanceof DerivativeException) {
            throw (DerivativeException) cause;
        } else if (cause instanceof EventException) {
            throw (EventException) cause;
        }
        throw new EventException(e);
    }
}
