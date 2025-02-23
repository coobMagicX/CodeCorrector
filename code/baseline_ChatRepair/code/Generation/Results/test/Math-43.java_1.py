public void addValue(double value) {
    sumImpl.increment(value);
    sumsqImpl.increment(value);
    minImpl.increment(value);
    maxImpl.increment(value);
    sumLogImpl.increment(value);
    secondMoment.increment(value);

    // Incrementing mean, variance and geometric mean only if their respective custom implementations are provided
    if (meanImpl != null && !(meanImpl instanceof Mean)) {
        meanImpl.increment(value);
    }
    if (varianceImpl != null && !(varianceImpl instanceof Variance)) {
        varianceImpl.increment(value);
    }
    if (geoMeanImpl != null && !(geoMeanImpl instanceof GeometricMean)) {
        geoMeanImpl.increment(value);
    }
    n++;
}
