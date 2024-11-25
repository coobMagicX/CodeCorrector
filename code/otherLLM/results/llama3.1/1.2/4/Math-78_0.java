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

        // this should never happen
        // instead of shifting ta to get opposite signs for ga and gb,
        // we simply ignore the event if it occurs between ta and tb
        // when interpolator.isForward() is true, but not when interpolator.isForward() is false
    if ((interpolator.isForward()) && (Math.abs(tb - previousEventTime) > convergence)) {
        // we were waiting for an event which was expected to occur during this step
        pendingEvent = false;
        pendingEventTime = Double.NaN;
        return true;
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
};
final BrentSolver solver = new BrentSolver();
solver.setAbsoluteAccuracy(convergence);
solver.setMaximalIterationCount(maxIterationCount);
final double root = (ta <= tb) ? solver.solve(f, ta, tb) : solver.solve(f, tb, ta);
if ((Math.abs(root - ta) <= convergence) &&
(Math.abs(root - previousEventTime) <= convergence)) {
// we have either found nothing or found (again ?) a past event, we simply ignore it
ta = tb;
ga = gb;
} else if (Double.isNaN(previousEventTime) ||
(Math.abs(previousEventTime - root) > convergence)) {
pendingEventTime = root;
if (pendingEvent && (Math.abs(t1 - pendingEventTime) <= convergence)) {
// we were already waiting for this event which was
// found during a previous call for a step that was
// rejected, this step must now be accepted since it
// properly ends exactly at the event occurrence
return false;
}
pendingEvent = true;
return true;
}

} catch (FunctionEvaluationException e) {
final Throwable cause = e.getCause();
if ((cause != null) && (cause instanceof DerivativeException)) {
throw (DerivativeException) cause;
} else if ((cause != null) && (cause instanceof EventException)) {
throw (EventException) cause;
}
throw new EventException(e);
}