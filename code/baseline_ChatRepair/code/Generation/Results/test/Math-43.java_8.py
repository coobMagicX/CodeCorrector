public void addValue(double value) {
    sumImpl.increment(value);
    sumsqImpl.increment(value);
    minImpl.increment(value);
    maxImpl.increment(value);
    sumLogImpl.increment(value);  // Ensure this is only incremented once per method call.
    secondMoment.increment(value);

    // Check overridden instances and increment as necessary.
    if (!(meanImpl instanceof Mean)) {
        meanImpl.increment(value);  // Handle the overridden mean.
    }
    
    if (!(varianceImpl instanceof Variance)) {
        varianceImpl.increment(value);  // Handle the overridden variance.
    }
    
    if (!(geoMeanImpl instanceof GeometricMean)) {
        geoMeanImpl.increment(value);  // Handle the overridden geometric mean.
    }

    n++;  // Increment the count
}
