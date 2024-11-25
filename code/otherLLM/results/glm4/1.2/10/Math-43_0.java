public void addValue(double value) {
    sumImpl.increment(value);
    sumsqImpl.increment(value);
    minImpl.increment(value);
    maxImpl.increment(value);
    sumLogImpl.increment(value);
    secondMoment.increment(value);

    // Increment mean if it has been overridden and is not an instance of Mean (implies a custom implementation)
    if (!(meanImpl instanceof Mean)) {
        double currentMean = meanImpl instanceof SummaryStatistics ? ((SummaryStatistics) meanImpl).getMean() : 0;
        meanImpl.increment(value);
        double newMean = meanImpl instanceof SummaryStatistics ? ((SummaryStatistics) meanImpl).getMean() : 0;
        
        // Adjust the value of n if necessary to account for the change in mean
        n += (newMean - currentMean) / newMean; // Assuming n is incremented based on the mean
    }

    // Increment variance and geometric mean similarly, assuming custom implementations
    if (!(varianceImpl instanceof Variance)) {
        double variance = varianceImpl instanceof SummaryStatistics ? ((SummaryStatistics) varianceImpl).getVariance() : 0;
        varianceImpl.increment(value);
        double newVariance = varianceImpl instanceof SummaryStatistics ? ((SummaryStatistics) varianceImpl).getVariance() : 0;

        // Adjust the value of n if necessary to account for the change in variance
        n += (newVariance - variance) / variance; // Assuming n is incremented based on the variance
    }

    if (!(geoMeanImpl instanceof GeometricMean)) {
        double geoMean = Math.exp(Math.log(geoMeanImpl instanceof SummaryStatistics ? ((SummaryStatistics) geoMeanImpl).getGeometricMean() : 1));
        geoMeanImpl.increment(value);
        double newGeoMean = Math.exp(Math.log(geoMeanImpl instanceof SummaryStatistics ? ((SummaryStatistics) geoMeanImpl).getGeometricMean() : 1));

        // Adjust the value of n if necessary to account for the change in geometric mean
        n += (newGeoMean - geoMean) / Math.log(newGeoMean); // Assuming n is incremented based on the geometric mean
    }

    n++;
}