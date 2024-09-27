public boolean evaluateStep(final StepInterpolator interpolator)
throws DerivativeException, EventException, ConvergenceException {

try {

    forward = interpolator.isForward();
    final double t1 = interpolator.getCurrentTime();
    final int    n  = Math.max(1, (int) Math.ceil(Math.abs(t1 - t0) / maxCheckInterval));
    final double h  = (t1 - t0) / n;

    double ta = t0;
    double ga = g0;
    double tb = t0 + (interpolator.isForward() ? convergence : -convergence);
    for (int i = 0; i < n; ++i) {

        // evaluate handler value at the end of the substep
        tb += h;
        interpolator.setInterpolatedTime(tb);
        final double gb = handler.g(tb, interpolator.getInterpolatedState());

        // check events occurrence
        if (g0Positive ^ (gb >= 0)) {
            // there is a sign change: an event is expected during this step

                // this is a corner case:
                // - there was an event near ta,
                // - there is another event between ta and tb
                // - when ta was computed, convergence was reached on the "wrong side" of the interval
                // this implies that the real sign of ga is the same as gb, so we need to slightly
                // shift ta to make sure ga and gb get opposite signs and the solver won't complain
                // about bracketing
                    // this should never happen
                throw new EventException("This should never happen");

        } else {
            // no sign change: there is no event for now
            ta = tb;
            ga = gb;
        }

    }

    // no event during the whole step
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