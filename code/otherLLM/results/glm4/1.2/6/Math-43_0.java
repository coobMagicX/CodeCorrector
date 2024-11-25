public void addValue(double value) {
    sumImpl.increment(value);
    sumsqImpl.increment(value);
    minImpl.increment(value);
    maxImpl.increment(value);
    sumLogImpl.increment(value);
    secondMoment.increment(value);

    // Corrected the increment calls for mean, variance and geomean by using methods provided
    if (!(meanImpl instanceof Mean)) {
        getMeanImpl().increment(value); // Use the correct method to increment mean
    }
    if (!(varianceImpl instanceof Variance)) {
        getVarianceImpl().increment(value); // Use the correct method to increment variance
    }
    if (!(geoMeanImpl instanceof GeometricMean)) {
        getGeoMeanImpl().increment(value); // Use the correct method to increment geomean
    }

    n++;
}