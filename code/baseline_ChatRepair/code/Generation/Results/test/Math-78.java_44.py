public boolean evaluateStep(final StepInterpolator interpolator)
    throws DerivativeException, EventException, ConvergenceException {

    try {

        forward = interpolator.isForward();
        final double t1 = interpolator.getCurrentTime();
        final int    n  = Math.max(1, (int) Math.ceil(Math.abs(t1 - t0) / maxCheckInterval));
        final double h  = (t1 - t0) / n;

        double ta = t0;
        double ga = g0;
        double tb = t0 + (interpolator.isForward() ? convergence : -convergence);
        for (int i = 0; i < n; ++i) {

            // evaluate handler value at the end of the substep
            tb += h;
            interpolator.setInterpolatedTime(tb);
            final double gb = handler.g(tb, interpolator.getInterpolatedState());

            // check events occurrence
            if (g0Positive ^ (gb >= 0)) {
                // The event occurred - handle root finding only if we are really bracketing a root:
                if (ga * gb < 0) { // ga and gb should have opposite signs
                    
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
                    final double root = (ta <= tb) ? solver.solve(f, ta, tb) : solver.solve(f, tb, ta);
                    // Check proximity to the last event or beginning of step
                    if ((Math.abs(root - ta) <= convergence) &&
                         (Math.abs(root - previousEventTime) <= convergence)) {
                        ta = tb;
                        ga = gb;
                    } else if (Double.isNaN(previousEventTime) ||
                               (Math.abs(previousEventTime - root) > convergence)) {
                        pendingEventTime = root;
                        pendingEvent = true;
                        return true;
                    }
                } else {
                    // Update ta and ga to continue search
                    ta = tb;
                    ga = gb;
                }
            } else {
                // no event detected, move to next possible event position
                ta = tb;
                ga = gb;
            }
        }

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
