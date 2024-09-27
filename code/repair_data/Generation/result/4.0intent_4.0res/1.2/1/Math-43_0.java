public class SummaryStatistics {
    private StorelessUnivariateStatistic sumImpl = new Sum();
    private StorelessUnivariateStatistic sumsqImpl = new SumOfSquares();
    private StorelessUnivariateStatistic minImpl = new Min();
    private StorelessUnivariateStatistic maxImpl = new Max();
    private StorelessUnivariateStatistic sumLogImpl = new SumOfLogs();
    private StorelessUnivariateStatistic geoMeanImpl = new GeometricMean();
    private StorelessUnivariateStatistic meanImpl = new Mean();
    private StorelessUnivariateStatistic varianceImpl = new Variance();
    private StorelessUnivariateStatistic secondMoment = new SecondMoment();
    private long n = 0;

    public void addValue(double value) {
        sumImpl.increment(value);
        sumsqImpl.increment(value);
        minImpl.increment(value);
        maxImpl.increment(value);
        sumLogImpl.increment(value);
        secondMoment.increment(value);
        geoMeanImpl.increment(value); // Correct usage of geoMeanImpl
        // If mean or variance have been overridden, need to increment these
        if (!(meanImpl instanceof Mean)) {
            meanImpl.increment(value);
        }
        if (!(varianceImpl instanceof Variance)) {
            varianceImpl.increment(value);
        }
        n++;
    }

    public void setGeoMeanImpl(StorelessUnivariateStatistic geoMeanImpl) {
        checkEmpty();
        this.geoMeanImpl = geoMeanImpl;
    }

    private void checkEmpty() {
        if (n > 0) {
            throw new IllegalStateException("Statistics cannot be changed once values have been added.");
        }
    }

    public double getGeometricMean() {
        return geoMeanImpl.getResult();
    }

    // Additional methods and class code would go here
}