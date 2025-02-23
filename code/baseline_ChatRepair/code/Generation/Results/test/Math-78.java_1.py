if (g0Positive ^ (gb >= 0)) {
    // Previously, this was solely the sign change detection condition
    // Now add additional checks to ensure that ga and gb have different signs

    if (Math.signum(ga) != Math.signum(gb)) {
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
        final double root;
        if (ta <= tb) {
            root = solver.solve(f, ta, tb, ga, gb);
        } else {
            root = solver.solve(f, tb, ta, gb, ga);
        }
        ...
    } else {
        // If the signs are the same, adjust ta and tb to search for a bracket change ahead
        ta = tb;
        ga = gb;
        tb += h;
        interpolator.setInterpolatedTime(tb);
        ga = handler.g(tb, interpolator.getInterpolatedState());
        if (Math.signum(ga) != Math.signum(gb)) {
            tb += h; // Ensure that tb goes further if still the same sign
        }
    }
} else {
    // no sign change: there is no event for now
    ta = tb;
    ga = gb;
}
