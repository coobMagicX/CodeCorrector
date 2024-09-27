public double integrate(final FirstOrderDifferentialEquations equations,
                        final double t0, final double[] y0,
                        final double t, final double[] y)
    throws DerivativeException, IntegratorException {

    sanityChecks(equations, t0, y0, t, y); // Check inputs
    setEquations(equations); // Set the differential equations to be used
    resetEvaluations(); // Reset state evaluations counter
    final boolean forward = t > t0; // Determine the integration direction

    // Create some internal working arrays
    final int stages = c.length + 1;
    if (y != y0) {
        System.arraycopy(y0, 0, y, 0, y0.length); // Copy initial state to the main state array if they are not the same
    }
    final double[][] yDotK = new double[stages][y0.length]; // Derivatives array
    final double[] yTmp = new double[y0.length]; // Temporary state array

    // Set up an interpolator sharing the integrator arrays
    AbstractStepInterpolator interpolator;
    if (requiresDenseOutput() || (!eventsHandlersManager.isEmpty())) {
        final RungeKuttaStepInterpolator rki = (RungeKuttaStepInterpolator) prototype.copy();
        rki.reinitialize(this, yTmp, yDotK, forward);
        interpolator = rki;
    } else {
        interpolator = new DummyStepInterpolator(yTmp, forward);
    }
    interpolator.storeTime(t0);

    // Set up integration control objects
    stepStart = t0;
    double hNew = 0;
    boolean firstTime = true;
    for (StepHandler handler : stepHandlers) {
        handler.reset();
    }
    CombinedEventsManager manager = addEndTimeChecker(t0, t, eventsHandlersManager);
    boolean lastStep = false;

    // Main integration loop
    while (!lastStep) {
        interpolator.shift();

        double error = 0;
        for (boolean loop = true; loop;) {
            if (firstTime || !fsal) {
                computeDerivatives(stepStart, y, yDotK[0]); // Compute first stage
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

            stepSize = hNew;

            // Next stages
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

            // Estimate the state at the end of the step
            for (int j = 0; j < y0.length; ++j) {
                double sum = b[0] * yDotK[0][j];
                for (int l = 1; l < stages; ++l) {
                    sum += b[l] * yDotK[l][j];
                }
                yTmp[j] = y[j] + stepSize * sum;
            }

            // Estimate the error at the end of the step
            error = estimateError(yDotK, y, yTmp, stepSize);
            if (error <= 1.0) {
                interpolator.storeTime(stepStart + stepSize);
                if (manager.evaluateStep(interpolator)) {
                    final double dt = manager.getEventTime() - stepStart;
                    if (Math.abs(dt) <= Math.ulp(stepStart)) {
                        loop = false; // Accepting the step
                    } else {
                        hNew = dt; // Reject the step to match exactly the next switch time
                    }
                } else {
                    loop = false; // Accept the step
                }
            } else {
                final double factor = Math.min(maxGrowth,
                                               Math.max(minReduction, safety * Math.pow(error, exp)));
                hNew = filterStep(stepSize * factor, forward, false);
            }
        }

        // The step has been accepted
        final double nextStep = stepStart + stepSize;
        System.arraycopy(yTmp, 0, y, 0, y0.length);
        manager.stepAccepted(nextStep, y);
        lastStep = manager.stop();

        // Provide the step data to the step handler
        interpolator.storeTime(nextStep);
        for (StepHandler handler : stepHandlers) {
            handler.handleStep(interpolator, lastStep);
        }
        stepStart = nextStep;

        if (fsal) {
            System.arraycopy(yDotK[stages - 1], 0, yDotK[0], 0, y0.length); // Save the last evaluation for the next step
        }

        if (manager.reset(stepStart, y) && !lastStep) {
            computeDerivatives(stepStart, y, yDotK[0]); // Recompute derivatives after an event
        }

        if (!lastStep) {
            stepSize = filterStep(stepSize, forward, true);

            // Stepsize control for next step
            final double factor = Math.min(maxGrowth,
                                           Math.max(minReduction,
                                                    safety * Math.pow(error, exp)));
            final double scaledH = stepSize * factor;
            final double nextT = stepStart + scaledH;
            final boolean nextIsLast = forward ? (nextT >= t) : (nextT <= t);
            hNew = filterStep(scaledH, forward, nextIsLast);
        }
    }

    final double stopTime = stepStart;
    resetInternalState();
    return stopTime;
}