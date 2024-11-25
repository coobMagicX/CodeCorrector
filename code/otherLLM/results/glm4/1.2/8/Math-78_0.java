public boolean evaluateStep(final StepInterpolator interpolator)
    throws DerivativeException, EventException, ConvergenceException {

    try {
        forward = interpolator.isForward();
        final double t1 = interpolator.getCurrentTime();
        final int n = Math.max(1, (int) Math.ceil(Math.abs(t1 - t0) / maxCheckInterval));
        final double h = (t1 - t0) / n;

        double ta = t0;
        double ga = g0;
        double tb = t0 + (forward ? convergence : -convergence);
        for (int i = 0; i < n; ++i) {

            // evaluate handler value at the end of the substep
            tb += h;
            interpolator.setInterpolatedTime(tb);
            final double gb = handler.g(tb, interpolator.getInterpolatedState());

            // check events occurrence
            if (g0Positive ^ (gb >= 0)) {
                // there is a sign change: an event is expected during this step

                // This should never happen as it's a corner case, but we'll handle it for completeness.
                if (Math.abs(ta - tb) <= convergence) {
                    // We need to adjust ta and ga to ensure that the solver will find the correct root
                    double adjustedTa = Math.min(Math.max(ta + h / 2, t0), t1);
                    interpolator.setInterpolatedTime(adjustedTa);
                    ga = handler.g(adjustedTa, interpolator.getInterpolatedState());
                }

                // variation direction, with respect to the integration direction
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
                double root;
                if (ta <= tb) {
                    root = solver.solve(f, ta, tb);
                } else {
                    root = solver.solve(f, tb, ta);
                }
                if ((Math.abs(root - ta) <= convergence) &&
                     (Math.abs(root - previousEventTime) > convergence)) {
                    // we have either found nothing or found a past event, we simply ignore it
                    ta = tb;
                    ga = gb;
                } else if (Double.isNaN(previousEventTime) ||
                           (Math.abs(previousEventTime - root) > convergence)) {
                    pendingEventTime = root;
                    if (pendingEvent && (Math.abs(t1 - pendingEventTime) <= convergence)) {
                        // we were already waiting for this event which was found during a previous call
                        return false;
                    }
                    pendingEvent = true;
                    return true;
                }

            } else {
                // no sign change: there is no event for now
                ta = tb;
                ga = gb;
            }

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