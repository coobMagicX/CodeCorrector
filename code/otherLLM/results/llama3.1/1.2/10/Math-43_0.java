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
        meanImpl = new Mean();  // Create a new instance of Mean and assign it to meanImpl
        meanImpl.increment(value);  // Increment the newly created Mean instance
    } else if (meanImpl != null) {  // Check if meanImpl is not null before incrementing
        meanImpl.increment(value);
    }

    if (!(varianceImpl instanceof Variance)) {
        varianceImpl = new Variance();  // Create a new instance of Variance and assign it to varianceImpl
        varianceImpl.increment(value);  // Increment the newly created Variance instance
    } else if (varianceImpl != null) {  // Check if varianceImpl is not null before incrementing
        varianceImpl.increment(value);
    }

    if (!(geoMeanImpl instanceof GeometricMean)) {
        geoMeanImpl = new GeometricMean();  // Create a new instance of GeometricMean and assign it to geoMeanImpl
        geoMeanImpl.increment(value);  // Increment the newly created GeometricMean instance
    } else if (geoMeanImpl != null) {  // Check if geoMeanImpl is not null before incrementing
        geoMeanImpl.increment(value);
    }

    n++;
}