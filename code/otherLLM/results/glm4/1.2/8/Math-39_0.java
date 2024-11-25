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
  double hNew      = 0;
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
        firstTime = false;
      }

      // Check for initial step size being too large
      if (firstTime && hNew > maxInitialStepSize) {
          hNew = maxInitialStepSize;  // Ensure the initial step size does not exceed maxInitialStepSize
      }
      
      stepSize = hNew;

      // next stages
      for (int k = 1; k < stages; ++k) {

        for (int j = 0; j < y0.length; ++j) {
          yDotK[k][j] = computeDerivatives(stepStart + k * stepSize / stages, y[j]);
        }

      }

      // Accept or adjust the step
      error = estimateError(yDotK);
      if (error < 1.0) {
          interpolator.storeTime(stepStart + stepSize);
          System.arraycopy(yTmp, 0, y, 0, y0.length);
          System.arraycopy(yDotK[stages - 1], 0, yDotTmp, 0, y0.length);
          stepStart = acceptStep(interpolator, y, yDotTmp, t);
          System.arraycopy(y, 0, yTmp, 0, y.length);
          break; // Exit the while loop if error is acceptable
      } else {
          final double factor =
              FastMath.min(maxGrowth, FastMath.max(minReduction, safety * FastMath.pow(error, exp)));
          hNew = filterStep(stepSize * factor, forward, false);
      }

    }

    // Prepare next step
    interpolator.storeTime(stepStart);

    if (!isLastStep) {

        // save the last evaluation for the next step
        System.arraycopy(yDotTmp, 0, yDotK[0], 0, y0.length);

        // Adjust next step size
        final double factor =
            FastMath.min(maxGrowth, FastMath.max(minReduction, safety * FastMath.pow(error, exp)));
        final double scaledH = stepSize * factor;
        final double nextT = stepStart + scaledH;
        final boolean nextIsLast = forward ? (nextT >= t) : (nextT <= t);
        hNew = filterStep(scaledH, forward, nextIsLast);

        if (filteredNextIsLast) {
            hNew = t - stepStart;
        }

    }

  } while (!isLastStep);

  // dispatch results
  equations.setTime(stepStart);
  equations.setCompleteState(y);

  resetInternalState();

}