for (int i = 0; i < n; ++i) {
    // evaluate handler value at the end of the substep
    tb += h;
    interpolator.setInterpolatedTime(tb);
    final double gb = handler.g(tb, interpolator.getInterpolatedState());

    // Check event occurrences by comparing the signs of ga and gb
    if ((ga >= 0 && gb < 0) || (ga < 0 && gb >= 0)) {
        // Corrected sign change detection logic
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
        
        // Rest of the event detection as before
        ...
    } else {
        // no sign change: there is no event for now
        ta = tb;
        ga = gb;
    }
}
