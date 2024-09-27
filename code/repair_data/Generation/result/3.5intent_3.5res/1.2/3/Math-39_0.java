// estimate the error at the end of the step
error = estimateError(yDotK, y, yTmp, stepSize);
if (error >= 1.0) {
    // reject the step and attempt to reduce error by stepsize control
    final double factor = FastMath.min(maxGrowth, FastMath.max(minReduction, safety * FastMath.pow(error, exp)));
    hNew = filterStep(stepSize * factor, forward, false);

    // additional check for large step size
    if (hNew > maxStep) {
        hNew = maxStep;
    }
}