public void addValue(double value) {
    sumImpl.increment(value);
    sumsqImpl.increment(value);
    minImpl.increment(value);
    maxImpl.increment(value);
    sumLogImpl.increment(value);
    secondMoment.increment(value);
    // Increment mean if the custom implementation is used
    if (meanImpl != null && !(meanImpl instanceof Mean)) {
        meanImpl.increment(value);
    }
    // Increment variance if the custom implementation is used
    if (varianceImpl != null && !(varianceImpl instanceof Variance)) {
        varianceImpl.increment(value);
    }
    // Increment geometric mean if the custom implementation is used
    if (geoMeanImpl != null && !(geoMeanImpl instanceof GeometricMean)) {
        geoMeanImpl.increment(value);
    }
    n++;
}