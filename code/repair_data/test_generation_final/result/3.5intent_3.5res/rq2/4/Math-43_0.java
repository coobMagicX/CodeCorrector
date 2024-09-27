public void addValue(double value) {
    sumImpl.increment(value);
    sumsqImpl.increment(value);
    minImpl.increment(value);
    maxImpl.increment(value);
    sumLogImpl.increment(value);
    secondMoment.increment(value);
    // If mean, variance or geomean have been overridden,
    // need to increment these
    if (!(meanImpl instanceof Mean)) {
        meanImpl.increment(value);
    } else {
        Mean mean = (Mean) meanImpl;
        mean.increment(value);
    }
    if (!(varianceImpl instanceof Variance)) {
        varianceImpl.increment(value);
    } else {
        Variance varImpl = (Variance) varianceImpl;
        varImpl.increment(value);
    }
    if (!(geoMeanImpl instanceof GeometricMean)) {
        geoMeanImpl.increment(value);
    }
    n++;
}