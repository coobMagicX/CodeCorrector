public void addValue(double value) {
    sumImpl.increment(value);
    sumsqImpl.increment(value);
    minImpl.increment(value);
    maxImpl.increment(value);
    sumLogImpl.increment(value);
    secondMoment.increment(value);

    // If mean, variance or geomean have been overridden,
    // need to increment these using the custom implementations
    if (meanImpl instanceof Mean) {
        ((Mean) meanImpl).increment(value);
    }
    if (varianceImpl instanceof Variance) {
        ((Variance) varianceImpl).increment(value);
    }
    if (geoMeanImpl instanceof GeometricMean) {
        ((GeometricMean) geoMeanImpl).increment(value);
    }

    n++;
}