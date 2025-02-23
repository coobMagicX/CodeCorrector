public boolean evaluateStep(final StepInterpolator interpolator)
    throws DerivativeException, EventException, ConvergenceException {

    try {

        forward = interpolator.isForward();
        final double t1 = interpolator.getCurrentTime();
        final int n = Math.max(1, (int) Math.ceil(Math.abs(t1 - t0) / maxCheckInterval));
        final double h = (t1 - t0) / n;

        double ta = t0;
        double ga = g0;
        double tb = ta; // Initialize tb to ta initially.

        for (int i = 0; i < n; ++i) {
            // Increment tb by step size h and evaluate the function g at tb
            tb += h;
            interpolator.setInterpolatedTime(tb);
            final double gb = handler.g(tb, interpolator.getInterpolatedState());

            // Check events occurrence: looking for sign changes
            if (g0Positive ^ (gb >= 0)) {
                // If there is a sign change and both ga and gb exist, use a solver
                if (!Double.isNaN(ga) && !Double.isNaN(gb)) {
                    increasing = gb >= ga;

                    final UnivariateRealSolver solver = new BrentSolver(convergence);
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

                    double root;
                    if (solver instanceof BaseSecantSolver) {
                        root = ((BaseSecantSolver)solver).solve(maxIterationCount, f, ta, tb, ga, gb);
                    } else {
                        root = solver.solve(maxIterationCount, f, ta, tb);
                    }

                    if ((Math.abs(root - ta) <= convergence) &&
                        (Math.abs(root - previousEventTime) <= convergence)) {
                        // Ignore closely spaced events
                        ta = tb;
                        ga = gb;
                    } else {
                        pendingEventTime = root;
                        pendingEvent = true;
                        return true;
                    }
                }
            } else {
                // Update ta and ga for the next iteration
                ta = tb;
                ga = gb;
            }
        }

        // if no event during the whole step
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
