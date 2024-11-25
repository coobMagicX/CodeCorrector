public void addValue(double value) {
    sumImpl.increment(value);
    sumsqImpl.increment(value);
    minImpl.increment(value);
    maxImpl.increment(value);
    sumLogImpl.increment(value);
    secondMoment.increment(value);

    // Increment mean if it has been overridden with a custom implementation.
    if (meanImpl != null) {
        meanImpl.increment(value);
    }
    
    // Increment variance if it has been overridden with a custom implementation.
    if (varianceImpl != null) {
        varianceImpl.increment(value);
    }

    // Increment geometric mean if it has been overridden with a custom implementation.
    if (geoMeanImpl != null) {
        geoMeanImpl.increment(value);
    }
    
    n++;
}