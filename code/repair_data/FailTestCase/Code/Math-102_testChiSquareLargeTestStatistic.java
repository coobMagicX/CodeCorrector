    public void testChiSquareLargeTestStatistic() throws Exception {
        double[] exp = new double[] {
                3389119.5, 649136.6, 285745.4, 25357364.76, 11291189.78, 543628.0, 
                232921.0, 437665.75
        };

        long[] obs = new long[] {
                2372383, 584222, 257170, 17750155, 7903832, 489265, 209628, 393899
        };
        org.apache.commons.math.stat.inference.ChiSquareTestImpl csti =
            new org.apache.commons.math.stat.inference.ChiSquareTestImpl(); 
        double cst = csti.chiSquareTest(exp, obs); 
        assertEquals("chi-square p-value", 0.0, cst, 1E-3);
        assertEquals( "chi-square test statistic", 
                114875.90421929007, TestUtils.chiSquare(exp, obs), 1E-9);
    }