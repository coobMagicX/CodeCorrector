public double integrate(final FirstOrderDifferentialEquations equations,
                        final double t0, final double[] y0,
                        final double t, final double[] y)
throws DerivativeException, IntegratorException {

  sanityChecks(equations, t0, y0, t, y);
  setEquations(equations);
  resetEvaluations();
  final boolean forward = t > t0;

  // create some internal working arrays
  final int stages = c.length + 1;
  if (y != y0) {
    System.arraycopy(y0, 0, y, 0, y0.length);
  }
  final double[][] yDotK = new double[stages][y0.length];
  final double[] yTmp = new double[y0.length];

  // set up an interpolator sharing the integrator arrays
  AbstractStepInterpolator interpolator;
  if (requiresDenseOutput() || (! eventsHandlersManager.isEmpty())) {
    final RungeKuttaStepInterpolator rki = (RungeKuttaStepInterpolator) prototype.copy();
    rki.reinitialize(this, yTmp, yDotK, forward);
    interpolator = rki;
  } else {
    interpolator = new DummyStepInterpolator(yTmp, forward);
  }
  interpolator.storeTime(t0);

  // set up integration control objects
  stepStart         = t0;
  final double stopTime = Math.min(Math.max(stepStart + safety * (t - stepStart), stepStart + hMin), t); // adjust the stopping time to be within the bounds of the input interval
  resetInternalState();
  return stopTime;

}

protected abstract double estimateError(double[][] yDotK,
                                          double[] y0, double[] y1,
                                          double h);