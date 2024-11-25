// main integration loop
while (!lastStep) {

    interpolator.shift();

    double error = 0;
    for (boolean loop = true; loop;) {

      if (firstTime || !fsal) {
        // first stage
        computeDerivatives(stepStart, y, yDotK[0]);
      }

      if (firstTime) {
        final double[] scale;
        if (vecAbsoluteTolerance == null) {
            scale = new double[y0.length];
            java.util.Arrays.fill(scale, scalAbsoluteTolerance);
          } else {
            scale = vecAbsoluteTolerance;
          }
        hNew = initializeStep(equations, forward, getOrder(), scale,
                              stepStart, y, yDotK[0], yTmp, yDotK[1]);
        firstTime = false;
      }

      stepSize = Math.max(filterStep(hNew, forward, false), minStep);

      // next stages
      for (int i = 1; i < c.length - 1; i++) {
        computeDerivatives(stepStart + c[i] * stepSize, y, yDotK[i]);
      }

      // the step has been accepted
      final double nextStep = stepStart + stepSize;
      System.arraycopy(yTmp, 0, y, 0, y0.length);
      manager.stepAccepted(nextStep, y);
      lastStep = manager.stop();

      // provide the step data to the step handler
      interpolator.storeTime(nextStep);
      for (StepHandler handler : stepHandlers) {
          handler.handleStep(interpolator, lastStep);
      }
      stepStart = nextStep;

      if (fsal) {
        // save the last evaluation for the next step
        System.arraycopy(yDotK[c.length - 1], 0, yDotK[0], 0, y0.length);
      }

      if (manager.reset(stepStart, y) && ! lastStep) {
        // some event handler has triggered changes that
        // invalidate the derivatives, we need to recompute them
        computeDerivatives(stepStart, y, yDotK[0]);
      }

      if (! lastStep) {
        // in some rare cases we may get here with stepSize = 0, for example
        // when an event occurs at integration start, reducing the first step
        // to zero; we have to reset the step to some safe non zero value
        stepSize = Math.max(filterStep(stepSize, forward, true), minStep);

        // stepsize control for next step
        final double factor = Math.min(maxGrowth,
                                       Math.max(minReduction,
                                                safety * Math.pow(error, exp)));
        final double  scaledH    = stepSize * factor;
        final double  nextT      = stepStart + scaledH;
        final boolean nextIsLast = forward ? (nextT >= t) : (nextT <= t);
        hNew = filterStep(scaledH, forward, nextIsLast);
      }

    }

    // reject the step and attempt to reduce error by stepsize control
    if (!loop) {
      error = estimateError(yDotK, y, yTmp, stepSize);
      final double factor =
          Math.min(maxGrowth,
                   Math.max(minReduction, safety * Math.pow(error, exp)));
      hNew = filterStep(stepSize * factor, forward, false);
    }

  }