public void addValue(double value) {
    sumImpl.increment(value);
    sumsqImpl.increment(value);
    minImpl.increment(value);
    maxImpl.increment(value);
    sumLogImpl.increment(value);
    secondMoment.increment(value);

    // If mean has been overridden, increment the custom Mean implementation
    if (meanImpl != null && meanImpl instanceof Mean) {
        ((Mean) meanImpl).increment(value);
    }

    // Increment variance and geomean only if their respective implementations are non-null
    // and they implement Variance or GeometricMean respectively.
    if (varianceImpl != null && varianceImpl instanceof Variance) {
        ((Variance) varianceImpl).increment(value);
    }
    if (geoMeanImpl != null && geoMeanImpl instanceof GeometricMean) {
        ((GeometricMean) geoMeanImpl).increment(value);
    }

    n++;
}