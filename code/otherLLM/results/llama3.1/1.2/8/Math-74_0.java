public class AdamsMoultonIntegrator {

    // ... existing methods and code ...

    public double integrate(final FirstOrderDifferentialEquations equations,
                             final double t0, final double[] y0,
                             final double t, final double[] y)
        throws DerivativeException, IntegratorException {

            sanityChecks(equations, t0, y0, t, y);
            setEquations(equations);
            resetEvaluations();
            final boolean forward = t > t0;

            // ... existing code ...

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

                    stepSize = hNew;

                    // ... existing code ...

                }  // end of inner while loop

                if (error <= 1.0) {

                    // discrete events handling
                    interpolator.storeTime(stepStart + stepSize);
                    if (manager.evaluateStep(interpolator)) {
                        final double dt = manager.getEventTime() - stepStart;
                        if (Math.abs(dt) <= Math.ulp(stepStart)) {
                            // rejecting the step would lead to a too small next step, we accept it
                            loop = false;
                        } else {
                            // reject the step to match exactly the next switch time
                            hNew = dt;
                        }
                    } else {
                        // accept the step
                        loop = false;
                    }

                } else {
                    // reject the step and attempt to reduce error by stepsize control
                    final double factor =
                        Math.min(maxGrowth,
                                 Math.max(minReduction, safety * Math.pow(error, exp)));
                    hNew = filterStep(stepSize * factor, forward, false);
                }

            }  // end of outer while loop

            final double stopTime = stepStart;
            resetInternalState();
            return stopTime;

        }  // end of integrate method
}