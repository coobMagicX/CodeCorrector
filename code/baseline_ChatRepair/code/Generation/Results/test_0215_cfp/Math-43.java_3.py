public void addValue(double value) {
    sumImpl.increment(value);
    sumsqImpl.increment(value);
    minImpl.increment(value);
    maxImpl.increment(value);
    sumLogImpl.increment(value);
    secondMoment.increment(value);

    if (!(meanImpl instanceof Mean)) {
        if (n == 0) {
            meanImpl = new Mean();
        }
        meanImpl.increment(value);
    }
    if (!(varianceImpl instanceof Variance)) {
        if (n == 0) {
            varianceImpl = new Variance();
        }
        varianceImpl.increment(value);
    }
    if (!(geoMeanImpl instanceof GeometricMean)) {
        if (n == 0) {
            geoMeanImpl = new GeometricMean();
        }
        geoMeanImpl.increment(value);
    }
    n++;
}
