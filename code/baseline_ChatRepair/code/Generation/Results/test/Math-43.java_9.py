public void addValue(double value) {
    sumImpl.increment(value);
    sumsqImpl.increment(value);
    minImpl.increment(value);
    maxImpl.increment(value);
    sumLogImpl.increment(value);
    secondMoment.increment(value);

    // Always increment these moments as they should be maintained continuously
    meanImpl.increment(value);
    varianceImpl.increment(value);
    geoMeanImpl.increment(value);

    n++;
}
