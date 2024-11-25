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
        meanImpl.addValue(value);  // Call addValue method instead of increment
    }
    if (!(varianceImpl instanceof Variance)) {
        varianceImpl.addValue(value);  // Call addValue method instead of increment
    }
    if (!(geoMeanImpl instanceof GeometricMean)) {
        geoMeanImpl.addValue(value);  // Call addValue method instead of increment
    }
    n++;
}