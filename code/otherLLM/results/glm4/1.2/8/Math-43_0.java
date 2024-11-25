public void addValue(double value) {
    sumImpl.increment(value);
    sumsqImpl.increment(value);
    minImpl.increment(value);
    maxImpl.increment(value);
    sumLogImpl.increment(value);
    secondMoment.increment(value);

    // Increment mean if an overridden Mean instance is used
    if (meanImpl instanceof Mean) {
        ((Mean) meanImpl).increment(value);
    }

    // Increment variance if an overridden Variance instance is used
    if (varianceImpl instanceof Variance) {
        ((Variance) varianceImpl).increment(value);
    }

    // Increment geometric mean if an overridden GeometricMean instance is used
    if (geoMeanImpl instanceof GeometricMean) {
        ((GeometricMean) geoMeanImpl).increment(value);
    }

    n++;
}