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
  final double[] yDotTmp = new double[y.length];

  // set up an interpolator sharing the integrator arrays
  final RungeKuttaStepInterpolator interpolator = (RungeKuttaStepInterpolator) prototype.copy();
  interpolator.reinitialize(this, yTmp, yDotK, forward,
                            equations.getPrimaryMapper(), equations.getSecondaryMappers());
  interpolator.storeTime(equations.getTime());

  // stepsize control for first step
  final double factor =
      FastMath.min(maxGrowth, FastMath.max(minReduction, safety * FastMath.pow(1.0, exp)));
  final double  scaledH    = filterStep((t - equations.getTime()) * factor, forward, true);
  final double  nextT      = equations.getTime() + scaledH;
  final boolean nextIsLast = forward ? (nextT >= t) : (nextT <= t);
  hNew = scaledH;

  while (!nextIsLast) {

    // local error is small enough: accept the step, trigger events and step handlers
    interpolator.storeTime(nextT);

    System.arraycopy(yTmp, 0, y, 0, y0.length);
    System.arraycopy(yDotK[stages - 1], 0, yDotTmp, 0, y0.length);
    nextT = acceptStep(interpolator, y, yDotTmp, t);
    System.arraycopy(y, 0, yTmp, 0, y.length);

    // stepsize control for next step
    final double error = estimateError(yDotK, y0, y1, scaledH);
    factor =
        FastMath.min(maxGrowth, FastMath.max(minReduction, safety * FastMath.pow(error, exp)));
    scaledH *= factor;
    nextT += scaledH;

    // check if we're done
    final boolean lastStep = forward ? (nextT >= t) : (nextT <= t);
    hNew = filterStep(scaledH, forward, lastStep);

  }

  // dispatch results
  equations.setTime(nextT);
  equations.setCompleteState(y);

  resetInternalState();

}