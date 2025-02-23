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
                increasing = gb >= ga;

                try {
                    final BrentSolver solver = new BrentSolver(convergence);
                    final double root = solver.solve(new UnivariateRealFunction() {
                        public double value(double t) throws FunctionEvaluationException {
                            try {
                                interpolator.setInterpolatedTime(t);
                                return handler.g(t, interpolator.getInterpolatedState());
                            } catch (DerivativeException e) {
                                throw new FunctionEvaluationException(e, t);
                            } catch (EventException e) {
                                throw new FunctionEvaluationException(e, t);
                            }
                        }
                    }, ta, tb);

                    if ((Math.abs(root - ta) > convergence) &&
                        (Math.abs(root - previousEventTime) > convergence)) {
                        pendingEventTime = root;
                        pendingEvent = true;
                        return true;
                    }
                } catch (ConvergenceException e) {
                    // Handle the situation where the solver fails due to lack of bracketing
                }
                
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
        if ((cause != null) && (cause instanceof DerivativeException)) {
            throw (DerivativeException) cause;
        } else if ((cause != null) && (cause instanceof EventException)) {
            throw (EventException) cause;
        }
        throw new EventException(e);
    }
}
