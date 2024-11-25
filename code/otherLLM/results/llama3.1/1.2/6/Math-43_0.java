public void addValue(double value) {
    sumImpl.increment(value);
    sumsqImpl.increment(value);
    minImpl.increment(value);
    maxImpl.increment(value);
    sumLogImpl.increment(value);
    secondMoment.increment(value);
    meanImpl.increment(value); // Corrected mean calculation
    varianceImpl.increment(value); // Corrected variance calculation
    geoMeanImpl.increment(value); // Corrected geometric mean increment call
    n++;
}