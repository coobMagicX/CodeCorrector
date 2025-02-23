public boolean evaluateStep(final StepInterpolator interpolator)
    throws DerivativeException, EventException, ConvergenceException {

    try {

        forward = interpolator.isForward();
        final double t1 = interpolator.getCurrentTime();
        final int n = Math.max(1, (int) Math.ceil(Math.abs(t1 - t0) / maxCheckInterval));
        final double h = (t1 - t0) / n;

        double ta = t0;
        double ga = g0;
        boolean eventOccured = false;

        for (int i = 0; i < n; ++i) {

            // evaluate handler value at the end of the substep
            double tb = ta + h;
            interpolator.setInterpolatedTime(tb);
            final double gb = handler.g(tb, interpolator.getInterpolatedState());

            // check if there is a sign change between ga and gb
            if (g0Positive ^ (gb >= 0)) {
                increasing = gb >= ga;
                
                final BrentSolver solver = new BrentSolver();
                solver.setAbsoluteAccuracy(convergence);
                solver.setMaximalIterationCount(maxIterationCount);
                
                // Ensure that ga and gb have opposite signs for the Brent solver to work.
                if ((ga >= 0 && gb <= 0) || (ga <= 0 && gb >= 0)) {
                    final double root = solver.solve(new UnivariateRealFunction() {
                        public double value(final double t) throws FunctionEvaluationException {
                            try {
                                interpolator.setInterpolatedTime(t);
                                return handler.g(t, interpolator.getInterpolatedState());
                            } catch (DerivativeException | EventException e) {
                                throw new FunctionEvaluationException(e, t);
                            }
                        }
                    }, ta, tb);

                    processEvent(root, t1);
                    eventOccured = true;
                    break; // Exit the loop after processing the event
                }
            }

            // update ta, ga for the next iteration
            ta = tb;
            ga = gb;
        }

        pendingEvent = eventOccured;
        return eventOccured;

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

private void processEvent(final double root, final double t1) {
    pendingEventTime = root;
    if (pendingEvent && (Math.abs(t1 - pendingEventTime) <= convergence)) {
        // the event was found during a previous call for a step that was rejected; acknowledge it
        pendingEvent = true;
    } else {
        pendingEvent = false;
    }
}
