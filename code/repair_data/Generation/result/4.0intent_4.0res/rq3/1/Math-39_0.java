public void integrate(final ExpandableStatefulODE equations, final double t)
throws MathIllegalStateException, MathIllegalArgumentException {

    sanityChecks(equations, t);
    setEquations(equations);
    final boolean forward = t > equations.getTime();

    // create some internal working arrays
    final double[] y0 = equations.getCompleteState();
    final double[] y = y0.clone();
    final int stages = c.length;
    final double[][] yDotK = new double[stages][y.length];
    final double[] yTmp = y0.clone();
    final double[] yDotTmp = new double[y.length];

    // set up an interpolator sharing the integrator arrays
    final RungeKuttaStepInterpolator interpolator = (RungeKuttaStepInterpolator) prototype.copy();
    interpolator.reinitialize(this, yTmp, yDotK, forward, equations.getPrimaryMapper(), equations.getSecondaryMappers());
    interpolator.storeTime(equations.getTime());

    // set up integration control objects
    double stepStart = equations.getTime();
    double hNew = 0;
    boolean firstTime = true;
    initIntegration(equations.getTime(), y0, t);

    // main integration loop
    boolean isLastStep = false;
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
                final double[] scale = new double[y.length];
                if (vecAbsoluteTolerance == null) {
                    for (int i = 0; i < scale.length; ++i) {
                        scale[i] = scalAbsoluteTolerance + scalRelativeTolerance * Math.abs(y[i]);
                    }
                } else {
                    for (int i = 0; i < scale.length; ++i) {
                        scale[i] = vecAbsoluteTolerance[i] + vecRelativeTolerance[i] * Math.abs(y[i]);
                    }
                }
                hNew = initializeStep(forward, getOrder(), scale, stepStart, y, yDotK[0], yTmp, yDotK[1]);
                firstTime = false;
            }

            double stepSize = hNew;

            // next stages
            for (int k = 1; k < stages; ++k) {
                for (int j = 0; j < y0.length; ++j) {
                    double sum = a[k - 1][0] * yDotK[0][j];
                    for (int l = 1; l < k; ++l) {
                        sum += a[k - 1][l] * yDotK[l][j];
                    }
                    yTmp[j] = y[j] + stepSize * sum;
                }

                computeDerivatives(stepStart + c[k - 1] * stepSize, yTmp, yDotK[k]);
            }

            // estimate the state at the end of the step
            for (int j = 0; j < y0.length; ++j) {
                double sum = b[0] * yDotK[0][j];
                for (int l = 1; l < stages; ++l) {
                    sum += b[l] * yDotK[l][j];
                }
                yTmp[j] = y[j] + stepSize * sum;
            }

            // estimate the error
            error = estimateError(yDotK, y, yTmp, stepSize);
            if (error >= 1.0) {
                // reject the step
                double factor = Math.min(maxGrowth, Math.max(minReduction, safety * Math.pow(error, exp)));
                hNew = filterStep(stepSize * factor, forward, false);
            }
        }

        // accept the step
        interpolator.storeTime(stepStart + hNew);
        System.arraycopy(yTmp, 0, y, 0, y0.length);
        System.arraycopy(yDotK[stages - 1], 0, yDotTmp, 0, y0.length);
        stepStart = acceptStep(interpolator, y, yDotTmp, t);
        System.arraycopy(y, 0, yTmp, 0, y.length);

        // prepare next step
        if (!isLastStep) {
            interpolator.storeTime(stepStart);
            if (fsal) {
                System.arraycopy(yDotTmp, 0, yDotK[0], 0, y0.length);
            }
            double factor = Math.min(maxGrowth, Math.max(minReduction, safety * Math.pow(error, exp)));
            double scaledH = stepSize * factor;
            double nextT = stepStart + scaledH;
            boolean nextIsLast = forward ? (nextT >= t) : (nextT <= t);
            hNew = filterStep(scaledH, forward, nextIsLast);
            double filteredNextT = stepStart + hNew;
            boolean filteredNextIsLast = forward ? (filteredNextT >= t) : (filteredNextT <= t);
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