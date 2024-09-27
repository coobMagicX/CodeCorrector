public void addValue(double value) {
    sumImpl.increment(value);
    sumsqImpl.increment(value);
    minImpl.increment(value);
    maxImpl.increment(value);
    sumLogImpl.increment(value);
    secondMoment.increment(value);
    // If mean, variance or geomean have been overridden,
    // need to increment these
    meanImpl.increment(value); // Move this line outside the if condition
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

public double getMean() {
    return meanImpl.getResult();
}

public double getVariance() {
    return varianceImpl.getResult();
}

public double getGeometricMean() {
    return geoMeanImpl.getResult();
}