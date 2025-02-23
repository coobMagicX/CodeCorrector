for (int i = 0; i < n; ++i) {
    // evaluate handler value at the end of the substep
    tb += h;
    interpolator.setInterpolatedTime(tb);
    final double gb = handler.g(tb, interpolator.getInterpolatedState());

    // check events occurrence
    if (g0Positive ^ (gb >= 0)) { // Check if there's a sign change
        if (ga * gb <= 0) { // Ensure proper bracketing
            // There is a sign change with proper bracketing: an event is expected during this step
            // Variation direction, with respect to the integration direction
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
            if ((Math.abs(root - ta) <= convergence) &&
                (Math.abs(root - previousEventTime) <= convergence)) {
                // Handle the event
            } else if (Double.isNaN(previousEventTime) ||
                       (Math.abs(previousEventTime - root) > convergence)) {
                // Handle new or shifted event
            }
        } else {
            // ga and gb do not bracket a root; depending on the situation, handle or log this
        }
    } else {
        // continue further steps
    }
    ta = tb;
    ga = gb;
}

// More logic
return false;
