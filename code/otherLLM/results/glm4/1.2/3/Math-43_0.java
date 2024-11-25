public void addValue(double value) {
    sumImpl.increment(value);
    sumsqImpl.increment(value);
    minImpl.increment(value);
    maxImpl.increment(value);
    sumLogImpl.increment(value);
    secondMoment.increment(value);

    // Increment the mean implementation if it is an instance of Mean
    if (meanImpl instanceof Mean) {
        ((Mean) meanImpl).increment(value);
    }

    // Increment the variance implementation if it is an instance of Variance
    if (varianceImpl instanceof Variance) {
        ((Variance) varianceImpl).increment(value);
    }

    // Increment the geometric mean implementation if it is an instance of GeometricMean
    if (geoMeanImpl instanceof GeometricMean) {
        ((GeometricMean) geoMeanImpl).increment(value);
    }

    n++;
}