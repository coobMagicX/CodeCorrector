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

  // set up integration control objects
  stepStart         = equations.getTime();
  double  hNew      = 0;
  boolean firstTime = true;
  initIntegration(equations.getTime(), y0, t);

  // main integration loop
  isLastStep = false;
  do {

    interpolator.shift();

    // iterate over step size, ensuring local normalized error is smaller than 1
    double error = 10;
    while (error >= 1.0) {

      if (firstTime || !fsal) {
        // first stage
        computeDerivatives(stepStart, y, yDotK[0]);
      }

      if (firstTime) {
        final double[] scale = new double[mainSetDimension];
        if (vecAbsoluteTolerance == null) {
            for (int i = 0; i < scale.length; ++i) {
              scale[i] = scalAbsoluteTolerance + scalRelativeTolerance * FastMath.abs(y[i]);
            }
        } else {
            for (int i = 0; i < scale.length; ++i) {
              scale[i] = vecAbsoluteTolerance[i] + vecRelativeTolerance[i] * FastMath.abs(y[i]);
            }
        }
        hNew = initializeStep(forward, getOrder(), scale,
                              stepStart, y, yDotK[0], yTmp, yDotK[1]);
        
        // Limit the initial step size to a reasonable value
        hNew = Math.min(hNew, maxInitialStepSize);
        
        firstTime = false;
      }

      stepSize = hNew;

      // next stages
      for (int k = 1; k < stages; ++k) {

        for (int j = 0; j < y.length; j++) {
          yDotK[k][j] = yDotK[0][j];
        }

        for (int j = 0; j < y.length; j++) {
          yTmp[j] += hNew * yDotK[k-1][j];
        }

      }

      // local error is small enough: accept the step, trigger events and step handlers
      interpolator.storeTime(stepStart + stepSize);
      System.arraycopy(yTmp, 0, y, 0, y.length);
      System.arraycopy(yDotK[stages - 1], 0, yDotTmp, 0, y.length);
      stepStart = acceptStep(interpolator, y, yDotTmp, t);
      System.arraycopy(y, 0, yTmp, 0, y.length);

      if (!isLastStep) {

        // prepare next step
        interpolator.storeTime(stepStart);

        if (fsal) {
            // save the last evaluation for the next step
            System.arraycopy(yDotTmp, 0, yDotK[0], 0, y.length);
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

    error = estimateError(yDotK, y0, yTmp, hNew);

  } while (!isLastStep && error >= 1.0);

  // dispatch results
  equations.setTime(stepStart);
  equations.setCompleteState(y);

  resetInternalState();

}