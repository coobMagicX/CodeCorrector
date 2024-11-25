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
        if (meanImpl != null) {
            meanImpl.increment(value);
        } else {
            copy(original.meanImpl, this.meanImpl); // assuming original has been set
            ((Mean)meanImpl).increment(value);
        }
    }
    if (!(varianceImpl instanceof Variance)) {
        if (varianceImpl != null) {
            varianceImpl.increment(value);
        } else {
            copy(original.varianceImpl, this.varianceImpl);
            ((Variance)varianceImpl).increment(value);
        }
    }
    if (!(geoMeanImpl instanceof GeometricMean)) {
        if (geoMeanImpl != null) {
            geoMeanImpl.increment(value);
        } else {
            copy(original.geoMeanImpl, this.geoMeanImpl);
            ((GeometricMean)geoMeanImpl).increment(value);
        }
    }
    n++;
}