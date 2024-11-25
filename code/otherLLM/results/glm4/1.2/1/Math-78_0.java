public boolean evaluateStep(final StepInterpolator interpolator)
throws DerivativeException, EventException, ConvergenceException {

    try {
        forward = interpolator.isForward();
        final double t1 = interpolator.getCurrentTime();
        final int n = Math.max(1, (int) Math.ceil(Math.abs(t1 - t0) / maxCheckInterval));
        final double h = (t1 - t0) / n;

        double ta = t0;
        double ga = g0;
        boolean g0Positive = Double.compare(ga, 0) > 0;
        double tb = t0 + (interpolator.isForward() ? h : -h);
        for (int i = 0; i < n; ++i) {
            // evaluate handler value at the end of the substep
            interpolator.setInterpolatedTime(tb);
            final double gb = handler.g(tb, interpolator.getInterpolatedState());

            // check events occurrence
            if (g0Positive != (gb >= 0)) {
                // there is a sign change: an event is expected during this step

                    // variation direction, with respect to the integration direction
                boolean increasing = gb >= ga;

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
                double root = ta;
                if ((Math.abs(tb - ta) <= convergence)) {
                    // If the interval is very small, the solver might not find a root.
                    root = (ta + tb) / 2;
                }
                final double foundRoot = solver.solve(f, ta, tb);
                if (Double.compare(foundRoot, root) == 0 || Double.compare(foundRoot, previousEventTime) <= 0) {
                    // we have either found nothing or found (again ?) a past event, we simply ignore it
                    ta = tb;
                    ga = gb;
                } else if (Double.isNaN(previousEventTime) || Double.compare(foundRoot, previousEventTime) > 0) {
                    pendingEventTime = foundRoot;
                    if (pendingEvent && Double.compare(t1, pendingEventTime) <= 0) {
                        // we were already waiting for this event which was
                        // found during a previous call for a step that was
                        // rejected, this step must now be accepted since it
                        // properly ends exactly at the event occurrence
                        return false;
                    }
                    // either we were not waiting for the event or it has
                    // moved in such a way the step cannot be accepted
                    pendingEvent = true;
                    return true;
                }

            } else {
                // no sign change: there is no event for now
                ta = tb;
                ga = gb;
                g0Positive = Double.compare(gb, 0) > 0; // Update g0Positive for the next iteration
            }
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