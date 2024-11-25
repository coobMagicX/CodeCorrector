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
  final double stopTime = t;
  final boolean lastStep = (stepStart == stopTime);
  final double[] yPrev = new double[y0.length];
  System.arraycopy(y0, 0, yPrev, 0, y0.length);

  while (!lastStep) {
    // the step has not been accepted
    interpolator.storeTime(stepStart);
    final boolean fsal = (stages == 1);

    do {
      // estimate the error at the end of the step
      double error = estimateError(yDotK, yPrev, y0, stepSize);
      if (!fsal) {
        // reject the step and attempt to reduce error by stepsize control
        final double factor =
            Math.min(maxGrowth,
                     Math.max(minReduction, safety * Math.pow(error, exp)));
        stepSize = filterStep(stepSize * factor, forward, false);
        error = estimateError(yDotK, yPrev, y0, stepSize);
      }

      // discrete events handling
      interpolator.storeTime(stepStart + stepSize);
      if (manager.evaluateStep(interpolator)) {
          final double dt = manager.getEventTime() - stepStart;
          if (Math.abs(dt) <= Math.ulp(stepStart)) {
              // rejecting the step would lead to a too small next step, we accept it
              break;
          } else {
              // reject the step to match exactly the next switch time
              stepSize = dt;
          }
      } else {
        // accept the step
        break;
      }

    } while (error > 1.0);

    // the step has been accepted
    final double nextStep = stepStart + stepSize;
    System.arraycopy(y0, 0, yPrev, 0, y0.length);
    manager.stepAccepted(nextStep, y0);
    lastStep = manager.stop();

    // provide the step data to the step handler
    interpolator.storeTime(nextStep);
    for (StepHandler handler : stepHandlers) {
        handler.handleStep(interpolator, lastStep);
    }
    stepStart = nextStep;

    if (fsal) {
      // save the last evaluation for the next step
      System.arraycopy(yDotK[stages - 1], 0, yDotK[0], 0, y0.length);
    }

    if (manager.reset(stepStart, y0)) {
      // some event handler has triggered changes that
      // invalidate the derivatives, we need to recompute them
      computeDerivatives(stepStart, y0, yDotK[0]);
    }
  }

  final double stopTime = stepStart;
  resetInternalState();
  return stopTime;

}