public void addValue(double value) {
    sumImpl.increment(value);
    sumsqImpl.increment(value);
    minImpl.increment(value);
    maxImpl.increment(value);
    sumLogImpl.increment(value);
    secondMoment.increment(value);
    
    // Increment or recalculate the necessary override statistics
    if (!(meanImpl instanceof Mean)) {
        // Recalculate or assert a different handling for meanImpl here
        meanImpl.increment(value);
    } else {
        meanImpl.increment(value); // Normal behavior
    }
    
    if (!(varianceImpl instanceof Variance)) {
        // Recalculate or assert a different handling for varianceImpl here
        varianceImpl.increment(value);
    } else {
        varianceImpl.increment(value); // Normal behavior
    }
    
    if (!(geoMeanImpl instanceof GeometricMean)) {
        // Recalculate or assert a different handling for geoMeanImpl here
        geoMeanImpl.increment(value);
    } else {
        geoMeanImpl.increment(value); // Normal behavior
    }
    
    n++; // Incrementing the count of values
}
