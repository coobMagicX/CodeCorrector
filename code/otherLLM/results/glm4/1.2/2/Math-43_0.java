public void addValue(double value) {
    sumImpl.increment(value);
    sumsqImpl.increment(value);
    minImpl.increment(value);
    maxImpl.increment(value);
    sumLogImpl.increment(value);
    secondMoment.increment(value);

    // Increment mean only if the meanImpl is an instance of Mean (the default implementation)
    if (meanImpl instanceof Mean) {
        ((Mean) meanImpl).increment(value);
    }

    // Increment variance only if the varianceImpl is an instance of Variance
    if (varianceImpl instanceof Variance) {
        ((Variance) varianceImpl).increment(value);
    }

    // Increment geometric mean only if the geoMeanImpl is an instance of GeometricMean
    if (geoMeanImpl instanceof GeometricMean) {
        ((GeometricMean) geoMeanImpl).increment(value);
    }

    n++;
}