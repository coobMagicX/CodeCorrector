public void addValue(double value) {
    sumImpl.increment(value);     // Maintain the sum total.
    sumsqImpl.increment(value);   // Maintain the sum of squares.
    minImpl.increment(value);     // Update the minimum.
    maxImpl.increment(value);     // Update the maximum.
    
    // Update sum of logs directly. As the issue seem to related to incorrect data handling with sumLogImpl
    sumLogImpl.increment(value);  // Update the sum of logarithms directly.

    secondMoment.increment(value);  // Track the second moment.
    
    // Check if the implementations are overridden, and increment accordingly.
    if (!(meanImpl instanceof Mean)) {
        meanImpl.increment(value);     // Doing things with custom mean implementation.
    } else {
        meanImpl.increment(value);     // Standard behaviour under normal circumstances.
    }
    
    if (!(varianceImpl instanceof Variance)) {
        varianceImpl.increment(value);  // Handling for custom variance implementation.
    } else {
        varianceImpl.increment(value);  // Standard behaviour under normal circumstances.
    }
    
    if (!(geoMeanImpl instanceof GeometricMean)) {
        geoMeanImpl.increment(value);   // For overridden geometric mean calculation.
    } else {
        geoMeanImpl.increment(value);   // Standard behaviour under normal conditions.
    }

    n++;  // Keep the count of the number