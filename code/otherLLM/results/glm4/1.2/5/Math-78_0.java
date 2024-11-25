public boolean evaluateStep(final StepInterpolator interpolator)
throws DerivativeException, EventException, ConvergenceException {

    try {
        final double t1 = interpolator.getCurrentTime();
        final double h = (t1 - t0) / Math.max(1, (int) Math.ceil(Math.abs(t1 - t0) / maxCheckInterval));
        
        double ta = t0;
        double ga = getEventHandler().g(ta, interpolator.getInterpolatedState());
        boolean g0Positive = Double.compare(ga, 0.0) > 0;

        double tb = ta + (interpolator.isForward() ? h : -h);
        for (int i = 0; i < Math.max(1, (int) Math.ceil(Math.abs(t1 - t0) / maxCheckInterval)); ++i) {
            interpolator.setInterpolatedTime(tb);
            double gb = getEventHandler().g(tb, interpolator.getInterpolatedState());

            if (g0Positive != (gb >= 0)) {
                final UnivariateRealFunction f = new UnivariateRealFunction() {
                    public double value(final double t) throws FunctionEvaluationException {
                        try {
                            interpolator.setInterpolatedTime(t);
                            return getEventHandler().g(t, interpolator.getInterpolatedState());
                        } catch (DerivativeException e) {
                            throw new FunctionEvaluationException(e, t);
                        } catch (EventException e) {
                            throw new FunctionEvaluationException(e, t);
                        }
                    }
                };
                final BrentSolver solver = new BrentSolver();
                solver.setAbsoluteAccuracy(getConvergence());
                solver.setMaximalIterationCount(maxIterationCount);
                double root;
                if (ta <= tb) {
                    root = solver.solve(f, ta, tb);
                } else {
                    root = solver.solve(f, tb, ta);
                }
                
                if ((Math.abs(root - ta) <= getConvergence()) && 
                    (!Double.isNaN(previousEventTime) || (Math.abs(previousEventTime - root) > getConvergence()))) {
                    ta = root;
                    ga = gb;
                } else {
                    pendingEventTime = ta;
                    pendingEvent = true;
                    return true;
                }
            }

            ta = tb;
            g0Positive = gb >= 0;
            tb += h;
        }

        // no event during the whole step
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