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
  double  hNew      = 0;
  boolean firstTime = true;
  for (StepHandler handler : stepHandlers) {
      handler.reset();
  }
  CombinedEventsManager manager = addEndTimeChecker(t0, t, eventsHandlersManager);
  boolean lastStep = false;

  // main integration loop
  while (!lastStep) {

    interpolator.shift();

    double error = 0;
    for (boolean loop = true; loop;) {

      if (firstTime || !fsal) {
        // first stage
        computeDerivatives(stepStart, y, yDotK[0]);
      }

      if (error == 0 && hNew > 0) {
        // accept the step
        loop = false;
      } else {
        // reject the step and attempt to reduce error by stepsize control
        final double factor =
            Math.min(maxGrowth,
                     Math.max(minReduction, safety * Math.pow(error, exp)));
        hNew = filterStep(hNew * factor, forward, false);
        computeDerivatives(stepStart + hNew, y, yDotK[0]);
      }

    }

    // the step has been accepted
    final double nextStep = stepStart + hNew;
    System.arraycopy(yTmp, 0, y, 0, y0.length);
    manager.stepAccepted(nextStep, y);
    lastStep = manager.stop();

    // provide the step data to the step handler
    interpolator.storeTime(nextStep);
    for (StepHandler handler : stepHandlers) {
        handler.handleStep(interpolator, lastStep);
    }
    stepStart = nextStep;

    if (! lastStep) {
      // in some rare cases we may get here with hNew = 0, for example
      // when an event occurs at integration start, reducing the first step
      // to zero; we have to reset the step to some safe non zero value
        hNew = filterStep(hNew, forward, true);

      // stepsize control for next step
      final double factor = Math.min(maxGrowth,
                                     Math.max(minReduction,
                                              safety * Math.pow(error, exp)));
      final double  scaledH    = hNew * factor;
      final double  nextT      = stepStart + scaledH;
      final boolean nextIsLast = forward ? (nextT >= t) : (nextT <= t);
      hNew = filterStep(scaledH, forward, nextIsLast);
    }

  }

  // update error estimate before returning stopTime
  computeDerivatives(stepStart, y, yDotK[0]);
  final double stopTime = stepStart;
  resetInternalState();
  return stopTime;

}