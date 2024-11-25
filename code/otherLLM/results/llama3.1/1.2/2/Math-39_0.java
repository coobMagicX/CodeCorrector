public void integrate(final ExpandableStatefulODE equations, final double t)
    throws MathIllegalStateException, MathIllegalArgumentException {

  sanityChecks(equations, t);
  setEquations(equations);
  final boolean forward = t > equations.getTime();

  // create some internal working arrays
  final double[] y0  = equations.getCompleteState();
  final double[] y = y0.clone();
  final int stages = c.length + 1;
  final double[][] yDotK = new double[stages][y.length];
  final double[] yTmp    = y0.clone();
  final double[] yDotTmp = y0.clone();
  final double[] yDotKLast = new double[y.length];

  // initialize the minimum step size to a very small value
  final double minStep = 1e-20;

  // check if the initial step size is smaller than the minimum step size
  double hNew;
  if (stepSize < minStep) {
    hNew = minStep;
  } else {
    hNew = stepSize;
  }

  do {

    // local error is small enough: accept the step, trigger events and step handlers
    interpolator.storeTime(stepStart + stepSize);
    System.arraycopy(yTmp, 0, y, 0, y0.length);
    System.arraycopy(yDotK[stages - 1], 0, yDotTmp, 0, y0.length);
    stepStart = acceptStep(interpolator, y, yDotTmp, t);
    System.arraycopy(y, 0, yTmp, 0, y.length);

    if (!isLastStep) {

        // prepare next step
        interpolator.storeTime(stepStart);

        if (fsal) {
            // save the last evaluation for the next step
            System.arraycopy(yDotTmp, 0, yDotK[0], 0, y0.length);
        }

        // stepsize control for next step
        final double factor =
            FastMath.min(maxGrowth, FastMath.max(minReduction, safety * FastMath.pow(error, exp)));
        final double  scaledH    = stepSize * factor;
        final double  nextT      = stepStart + scaledH;
        final boolean nextIsLast = forward ? (nextT >= t) : (nextT <= t);
        hNew = filterStep(scaledH, forward, nextIsLast);

        final double  filteredNextT      = stepStart + hNew;
        final boolean filteredNextIsLast = forward ? (filteredNextT >= t) : (filteredNextT <= t);
        if (filteredNextIsLast) {
            hNew = t - stepStart;
        }

    }

    // reject the step and attempt to reduce error by stepsize control
    interpolator.storeTime(stepStart + hNew);

    System.arraycopy(y0, 0, yTmp, 0, y0.length);
    final double[] tempYDotK = new double[y.length];
    if (fsal) {
        // save the last evaluation for the next step
        System.arraycopy(yDotK[stages - 1], 0, tempYDotK, 0, y0.length);
    }

    System.arraycopy(yTmp, 0, y, 0, y0.length);

    final double[] yDotKNew = new double[y.length];
    estimateError(yDotK, y, yTmp, hNew);
    error = FastMath.max(error, 1.0 / hNew); // ensure the error is not too small

    if (error >= 1.0) {
        // reject the step and attempt to reduce error by stepsize control
        final double factor =
            FastMath.min(maxGrowth,
                         FastMath.max(minReduction, safety * FastMath.pow(error, exp)));
        hNew = filterStep(hNew * factor, forward, false);
    }

  } while (!isLastStep);

  // dispatch results
  equations.setTime(stepStart);
  equations.setCompleteState(y);

  resetInternalState();

}