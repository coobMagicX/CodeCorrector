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
  int evaluations = 0; // New variable to keep track of the number of evaluations
  boolean lastStepAccepted = false; // New variable to indicate if the last step was accepted

  while (!lastStepAccepted) {
    while (true) {

      // the step has been accepted
      final double nextStep = stepStart + hNew;
      System.arraycopy(yTmp, 0, y, 0, y0.length);
      manager.stepAccepted(nextStep, y);

      interpolator.storeTime(nextStep);
      for (StepHandler handler : stepHandlers) {
          handler.handleStep(interpolator, lastStep);
      }
      stepStart = nextStep;

      if (fsal) {
        // save the last evaluation for the next step
        System.arraycopy(yDotK[stages - 1], 0, yDotK[0], 0, y0.length);
      }

      if (manager.reset(stepStart, y) && !lastStep) {
        // some event handler has triggered changes that
        // invalidate the derivatives, we need to recompute them
        computeDerivatives(stepStart, y, yDotK[0]);
      }

      if (!lastStep) {
        // in some rare cases we may get here with stepSize = 0, for example
        // when an event occurs at integration start, reducing the first step
        // to zero; we have to reset the step to some safe non zero value
          hNew = filterStep(hNew, forward, true);

        // stepsize control for next step
        final double factor = Math.min(maxGrowth,
                                       Math.max(minReduction,
                                                safety * Math.pow(estimateError(yDotK, y0, yTmp, hNew), exp)));
        final double  scaledH    = hNew * factor;
        final double  nextT      = stepStart + scaledH;
        final boolean nextIsLast = forward ? (nextT >= t) : (nextT <= t);
        hNew = filterStep(scaledH, forward, nextIsLast);

        evaluations++; // Increment the number of evaluations
      } else {
        lastStepAccepted = true; // Set the flag to indicate that the last step was accepted
      }

    }
  }

  final double stopTime = stepStart;
  resetInternalState();
  return stopTime;

}