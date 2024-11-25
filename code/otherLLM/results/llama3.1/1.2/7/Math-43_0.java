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
        ((Mean)meanImpl).updateValue(value); // Update value instead of increment
    }
    if (!(varianceImpl instanceof Variance)) {
        ((Variance)varianceImpl).updateValue(value); // Update value instead of increment
    }
    if (!(geoMeanImpl instanceof GeometricMean)) {
        ((GeometricMean)geoMeanImpl).updateValue(value); // Update value instead of increment
    }
    n++;
}