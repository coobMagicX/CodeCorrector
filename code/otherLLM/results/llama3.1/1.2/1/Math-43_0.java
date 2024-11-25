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
        meanImpl = new Mean(); // Initialize meanImpl with a new instance of Mean
        meanImpl.increment(value); // Increment the new meanImpl instance
    } else {
        meanImpl.increment(value);
    }
    if (!(varianceImpl instanceof Variance)) {
        varianceImpl = new Variance(); // Initialize varianceImpl with a new instance of Variance
        varianceImpl.increment(value); // Increment the new varianceImpl instance
    } else {
        varianceImpl.increment(value);
    }
    if (!(geoMeanImpl instanceof GeometricMean)) {
        geoMeanImpl = new GeometricMean(); // Initialize geoMeanImpl with a new instance of GeometricMean
        geoMeanImpl.increment(value); // Increment the new geoMeanImpl instance
    } else {
        geoMeanImpl.increment(value);
    }
    n++;
}