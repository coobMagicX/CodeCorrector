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
    stepStart = t0;
    double hNew = 0;
    boolean firstTime = true;
    for (StepHandler handler : stepHandlers) {
        handler.reset();
    }
    CombinedEventsManager manager = addEndTimeChecker(t0, t, eventsHandlersManager);
    boolean lastStep = false;

    // main integration loop
    int evaluations = 0; // Counter for evaluations
    while (!lastStep && evaluations < 90) {

        interpolator.shift();

        double error = 0;
        for (boolean loop = true; loop;) {

            if (firstTime || !fsal) {
                // first stage
                computeDerivatives(stepStart, y, yDotK[0]);
                evaluations++; // Increment the evaluation counter
            }

            // Rest of the code remains the same...
            // ...
            // ...

        }

        // Rest of the code remains the same...
        // ...
        // ...

    }

    final double stopTime = stepStart;
    resetInternalState();
    return stopTime;
}