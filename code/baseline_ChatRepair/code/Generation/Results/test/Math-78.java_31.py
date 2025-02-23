for (int i = 0; i < n; ++i) {
    // Set tb to the end of the current subinterval
    tb = ta + h;

    // Interpolate to tb and evaluate the function
    interpolator.setInterpolatedTime(tb);
    final double gb = handler.g(tb, interpolator.getInterpolatedState());

    // Check for zero crossing between ga and gb
    if (g0Positive ^ (gb >= 0)) {  // Check if there is a sign change
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

        try {
            final double root = (ta <= tb) ? solver.solve(f, ta, tb) : solver.solve(f, tb, ta);

            if (!(Math.abs(root - ta) <= convergence || Math.abs(root - previousEventTime) <= convergence)) {
                pendingEventTime = root;
                pendingEvent = true;
                return true;
            }
        } catch (FunctionEvaluationException e) {
            ta = tb;  // Move to the next subinterval
            ga = gb;
            continue;
        }
    }

    // Update the interval endpoints
    ta = tb;
    ga = gb;
}

pendingEvent = false;
pendingEventTime = Double.NaN;
return false;
