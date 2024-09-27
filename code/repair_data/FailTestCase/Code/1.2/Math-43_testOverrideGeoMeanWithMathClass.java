    public void testOverrideGeoMeanWithMathClass() throws Exception {
        double[] scores = {1, 2, 3, 4};
        SummaryStatistics stats = new SummaryStatistics();
        stats.setGeoMeanImpl(new GeometricMean()); 
        for(double i : scores) {
          stats.addValue(i);
        }
        Assert.assertEquals((new GeometricMean()).evaluate(scores),stats.getGeometricMean(), 0); 
    }