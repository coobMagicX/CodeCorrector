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
        firstTime = false;
      }

      // Apply a heuristic to ensure the step size does not grow too large
      if (hNew > maxStepSize) {
        hNew = maxStepSize; // Limit the initial step size to prevent overflow or instability
      }

      error = computeError(stepStart, y, yDotK[0], hNew); // Compute error for the current step

      // If the error is still too high after the first step, adjust the step size more conservatively
      if (error >= 1.0) {
        final double factor = safety * FastMath.pow(error, exp);
        hNew *= Math.min(maxGrowth, Math.max(minReduction, factor)); // Adjust the step size within allowed limits
      }

    }

    // local error is small enough: accept the step, trigger events and step handlers
    interpolator.storeTime(stepStart + hNew);
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
        final double factor = safety * FastMath.pow(error, exp);
        final double scaledH = hNew * Math.min(maxGrowth, Math.max(minReduction, factor));
        final double nextT = stepStart + scaledH;
        final boolean nextIsLast = forward ? (nextT >= t) : (nextT <= t);
        hNew = filterStep(scaledH, forward, nextIsLast);

        if (!forward && (hNew < 0)) {
            // If the step size is negative and we are integrating backwards, set it to zero
            hNew = 0;
        }

        final double filteredNextT = stepStart + hNew;
        final boolean filteredNextIsLast = forward ? (filteredNextT >= t) : (filteredNextT <= t);
        if (filteredNextIsLast) {
            hNew = t - stepStart; // Adjust the step size to ensure we reach the desired time
        }
    }

  } while (!isLastStep);

  // dispatch results
  equations.setTime(stepStart);
  equations.setCompleteState(y);

  resetInternalState();
}