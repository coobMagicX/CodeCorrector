public void addValue(double value) {
    sumImpl.increment(value);
    sumsqImpl.increment(value);
    minImpl.increment(value);
    maxImpl.increment(value);
    sumLogImpl.increment(value);
    secondMoment.increment(value);
    
    // Properly handle overridden mean, variance or geometric mean
    if (!(meanImpl instanceof Mean)) {
        if (meanImpl == null) meanImpl = new Mean();
        meanImpl.increment(value);
    }
    if (!(varianceImpl instanceof Variance)) {
        if (varianceImpl == null) varianceImpl = new Variance();
        varianceImpl.increment(value);
    }
    if (!(geoMeanImpl instanceof GeometricMean)) {
        if (geoMeanImpl == null) geoMeanImpl = new GeometricMean();
        geoMeanImpl.increment(value);
    }
    n++;
}
