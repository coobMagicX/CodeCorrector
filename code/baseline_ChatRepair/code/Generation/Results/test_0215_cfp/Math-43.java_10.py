public void addValue(double value) {
    sumImpl.increment(value);
    sumsqImpl.increment(value);
    minImpl.increment(value);
    maxImpl.increment(value);
    sumLogImpl.increment(value);
    secondMoment.increment(value);

    // Only increment mean, variance, or geometric mean if their implementations are standard,
    // otherwise, the setters are expected to replace them with custom implementations.
    if (meanImpl.getClass().equals(Mean.class)) {
        meanImpl.increment(value);
    }
    if (varianceImpl.getClass().equals(Variance.class)) {
        varianceImpl.increment(value);
    }
    if (geoMeanImpl.getClass().equals(GeometricMean.class)) {
        geoMeanImpl.increment(value);
    }

    n++;
}
