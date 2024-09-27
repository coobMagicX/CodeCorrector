public void addValue(double value) {
    sumImpl.increment(value);
    sumsqImpl.increment(value);
    minImpl.increment(Math.min(minImpl.getResult(), value));
    maxImpl.increment(Math.max(maxImpl.getResult(), value));
    sumLogImpl.increment(Math.log(value));
    secondMoment.increment(value * value);
    // If mean, variance or geomean have been overridden,
    // need to increment these
    if (!(meanImpl instanceof Mean)) {
        meanImpl.increment(value);
    }
    if (!(varianceImpl instanceof Variance)) {
        varianceImpl.increment(value);
    }
    if (!(geoMeanImpl instanceof GeometricMean)) {
        geoMeanImpl.increment(value);
    }
    n++;
}