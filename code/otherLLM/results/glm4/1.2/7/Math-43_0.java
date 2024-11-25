public void addValue(double value) {
    sumImpl.increment(value);
    sumsqImpl.increment(value);
    minImpl.increment(value);
    maxImpl.increment(value);
    sumLogImpl.increment(value);
    secondMoment.increment(value);

    // If mean, variance or geomean have been overridden,
    // need to increment these with the correct instances
    if (meanImpl != null && meanImpl instanceof Mean) {
        meanImpl.evaluate(value); // Assuming evaluate is the method that calculates the mean
    }
    if (varianceImpl != null && varianceImpl instanceof Variance) {
        varianceImpl.increment(value);
    }
    if (geoMeanImpl != null && geoMeanImpl instanceof GeometricMean) {
        geoMeanImpl.increment(value);
    }

    n++;
}