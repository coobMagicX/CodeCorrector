public void addValue(double value) {
    sumImpl.increment(value);
    sumsqImpl.increment(value);
    minImpl.increment(value);
    maxImpl.increment(value);
    sumLogImpl.increment(value);
    secondMoment.increment(value);
    // If mean, variance or geomean have been overridden,
    // need to increment these
    if (!(meanImpl instanceof Mean) || meanImpl.getClass().equals(Mean.class)) {
        meanImpl.increment(value);
    }
    if (!(varianceImpl instanceof Variance) || varianceImpl.getClass().equals(Variance.class)) {
        varianceImpl.increment(value);
    }
    if (!(geoMeanImpl instanceof GeometricMean) || geoMeanImpl.getClass().equals(GeometricMean.class)) {
        geoMeanImpl.increment(value);
    }
    n++;
}