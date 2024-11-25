public boolean evaluateStep(final StepInterpolator interpolator)
        throws DerivativeException, EventException, ConvergenceException {

    try {
        // Update the current state before starting the loop
        stepAccepted(interpolator.getCurrentTime(), interpolator.getCurrentState());

        forward = interpolator.isForward();
        final double t1 = interpolator.getCurrentTime();
        final int n = Math.max(1, (int) Math.ceil(Math.abs(t1 - t0) / maxCheckInterval));
        final double h = (t1 - t0) / n;

        double ta = t0;
        double ga = g0;
        double tb = t0 + (interpolator.isForward() ? h : -h);
        for (int i = 0; i < n; ++i) {

            interpolator.setInterpolatedTime(tb);
            final double gb = handler.g(tb, interpolator.getCurrentState());

            // Check for event occurrence
            if ((g0Positive != null && g0Positive ^ (gb >= 0)) || (g0Positive == null && ga >= 0 != gb >= 0)) {
                // This is a corner case where the sign of the derivative changes, indicating an event.
                // This should never happen because we would have already handled it in stepAccepted if necessary.

                increasing = gb >= ga;

                final UnivariateRealFunction f = new UnivariateRealFunction() {
                    public double value(double t) throws FunctionEvaluationException {
                        try {
                            interpolator.setInterpolatedTime(t);
                            return handler.g(t, interpolator.getCurrentState());
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
                double root = (ta <= tb) ? solver.solve(f, ta, tb) : solver.solve(f, tb, ta);

                // Check if the found root is within tolerance and not a past event
                if ((Math.abs(root - ta) <= convergence) && (!Double.isNaN(previousEventTime) || Math.abs(previousEventTime - root) > convergence)) {
                    pendingEventTime = root;
                    if (pendingEvent && (Math.abs(t1 - pendingEventTime) <= convergence)) {
                        // If the event was already expected, accept the step
                        return false;
                    }
                    // Otherwise, signal that an event has occurred and the step should be rejected
                    pendingEvent = true;
                    return true;
                }
            }

            ta = tb; // Update ta to the current end of substep
            ga = gb; // Update ga to the last known derivative value

            tb += h; // Move to the next substep
        }

        // No event during the whole step, so clear pending event flags
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