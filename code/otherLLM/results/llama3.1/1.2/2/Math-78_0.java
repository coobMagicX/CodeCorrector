public boolean evaluateStep(final StepInterpolator interpolator)
    throws DerivativeException, EventException, ConvergenceException {

    try {
        final boolean forward = interpolator.isForward();
        final double t1 = interpolator.getCurrentTime();

        int n = Math.max(1, (int) Math.ceil(Math.abs(t1 - this.t0) / maxCheckInterval));
        final double h = (t1 - this.t0) / n;

        double ta = this.t0;
        double ga = this.g0;
        double tb = this.t0 + (forward ? convergence : -convergence);

        for (int i = 0; i < n; ++i) {
            // Evaluate handler value at the end of the substep
            tb += h;
            interpolator.setInterpolatedTime(tb);
            final double gb = this.handler.g(tb, interpolator.getInterpolatedState());

            // Check for events occurrence
            if ((this.g0Positive ^ (gb >= 0))) {
                // There is a sign change: an event is expected during this step

                    // This should never happen, but handle it explicitly to ensure robustness.
                    // Handle the corner case where there's another event between ta and tb.

                // Variation direction, with respect to the integration direction
                this.increasing = gb >= ga;

                final UnivariateRealFunction f = new UnivariateRealFunction() {
                    public double value(final double t) throws FunctionEvaluationException {
                        try {
                            interpolator.setInterpolatedTime(t);
                            return this.handler.g(t, interpolator.getInterpolatedState());
                        } catch (DerivativeException e) {
                            throw new FunctionEvaluationException(e, t);
                        } catch (EventException e) {
                            throw new FunctionEvaluationException(e, t);
                        }
                    }
                };

                final BrentSolver solver = new BrentSolver();
                solver.setAbsoluteAccuracy(convergence);
                solver.setMaximalIterationCount(maximalIterationCount);

                final double root = (ta <= tb) ? solver.solve(f, ta, tb) : solver.solve(f, tb, ta);
                if ((Math.abs(root - ta) <= convergence) && (Math.abs(root - previousEventTime) <= convergence)) {
                    // We have either found nothing or found a past event; simply ignore it
                    ta = tb;
                    ga = gb;
                } else if (Double.isNaN(previousEventTime) || (Math.abs(previousEventTime - root) > convergence)) {
                    this.pendingEventTime = root;

                    if (this.pendingEvent && Math.abs(t1 - this.pendingEventTime) <= convergence) {
                        // We were already waiting for this event which was found during a previous call
                        // for a step that was rejected; this step must now be accepted since it ends exactly at the event occurrence
                        return false;
                    }

                    // Either we weren't waiting for the event or it has moved in such a way that the step cannot be accepted
                    this.pendingEvent = true;

                    return true;
                }
            } else {
                // No sign change: there is no event for now
                ta = tb;
                ga = gb;
            }

        }

        // No event during the whole step
        this.pendingEvent = false;
        this.pendingEventTime = Double.NaN;

        return false;

    } catch (FunctionEvaluationException e) {
        final Throwable cause = e.getCause();
        if ((cause != null) && (cause instanceof DerivativeException)) {
            throw new DerivativeException(e.getMessage(), e.getOriginalCause());
        } else if ((cause != null) && (cause instanceof EventException)) {
            throw new EventException(e.getMessage(), e.getOriginalCause());
        }

        // Handle other types of exceptions or provide additional logging
    }
}