public void addValue(double value) {
    // Increment basic statistical moments
    sumImpl.increment(value);
    sumsqImpl.increment(value);
    minImpl.increment(value);
    maxImpl.increment(value);
    secondMoment.increment(value);

    // Increment log sum; it must only be called once for each value
    sumLogImpl.increment(value);

    // Always use the possibly overridden implementations rather than checking class types.
    // It's assumed setters ensure the correct behavior and initialization
    meanImpl.increment(value);
    varianceImpl.increment(value);
    geoMeanImpl.increment(value);

    // Increment counter
    n++;
}
