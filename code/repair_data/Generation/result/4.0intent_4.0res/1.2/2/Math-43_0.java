public class SummaryStatistics {
    private StorelessUnivariateStatistic sumImpl;
    private StorelessUnivariateStatistic sumsqImpl;
    private StorelessUnivariateStatistic minImpl;
    private StorelessUnivariateStatistic maxImpl;
    private StorelessUnivariateStatistic sumLogImpl;
    private StorelessUnivariateStatistic secondMoment;
    private StorelessUnivariateStatistic meanImpl;
    private StorelessUnivariateStatistic varianceImpl;
    private StorelessUnivariateStatistic geoMeanImpl;
    private long n;

    public SummaryStatistics() {
        sumImpl = new Sum();
        sumsqImpl = new SumOfSquares();
        minImpl = new Min();
        maxImpl = new Max();
        sumLogImpl = new SumOfLogs();
        secondMoment = new SecondMoment();
        meanImpl = new Mean();
        varianceImpl = new Variance();
        geoMeanImpl = new GeometricMean();
        n = 0;
    }

    public void addValue(double value) {
        sumImpl.increment(value);
        sumsqImpl.increment(value);
        minImpl.increment(value);
        maxImpl.increment(value);
        sumLogImpl.increment(value);
        secondMoment.increment(value);
        // Increment mean, variance or geomean as they have been overridden
        meanImpl.increment(value);
        varianceImpl.increment(value);
        geoMeanImpl.increment(value);
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

    // Additional methods and class details would go here...
}