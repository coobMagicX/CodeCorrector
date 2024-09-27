    public void testChiSquare() throws Exception {
        
        // Target values computed using R version 1.8.1 
        // Some assembly required ;-)  
        //      Use sum((obs - exp)^2/exp) for the chi-square statistic and
        //      1 - pchisq(sum((obs - exp)^2/exp), length(obs) - 1) for the p-value
        
        long[] observed = {10, 9, 11};
        double[] expected = {10, 10, 10};
        assertEquals("chi-square statistic", 0.2,  TestUtils.chiSquare(expected, observed), 10E-12);
        assertEquals("chi-square p-value", 0.904837418036, TestUtils.chiSquareTest(expected, observed), 1E-10);
        
        long[] observed1 = { 500, 623, 72, 70, 31 };
        double[] expected1 = { 485, 541, 82, 61, 37 };
        assertEquals( "chi-square test statistic", 9.023307936427388, TestUtils.chiSquare(expected1, observed1), 1E-10);
        assertEquals("chi-square p-value", 0.06051952647453607, TestUtils.chiSquareTest(expected1, observed1), 1E-9);
        assertTrue("chi-square test reject", TestUtils.chiSquareTest(expected1, observed1, 0.07));
        assertTrue("chi-square test accept", !TestUtils.chiSquareTest(expected1, observed1, 0.05));

        try {
            TestUtils.chiSquareTest(expected1, observed1, 95);
            fail("alpha out of range, IllegalArgumentException expected");
        } catch (IllegalArgumentException ex) {
            // expected
        }  
        
        long[] tooShortObs = { 0 };
        double[] tooShortEx = { 1 };
        try {
            TestUtils.chiSquare(tooShortEx, tooShortObs);
            fail("arguments too short, IllegalArgumentException expected");
        } catch (IllegalArgumentException ex) {
            // expected
        }

        // unmatched arrays
        long[] unMatchedObs = { 0, 1, 2, 3 };
        double[] unMatchedEx = { 1, 1, 2 };
        try {
            TestUtils.chiSquare(unMatchedEx, unMatchedObs);
            fail("arrays have different lengths, IllegalArgumentException expected");
        } catch (IllegalArgumentException ex) {
            // expected
        }
        
        // 0 expected count
        expected[0] = 0;
        try {
            TestUtils.chiSquareTest(expected, observed, .01);
            fail("bad expected count, IllegalArgumentException expected");
        } catch (IllegalArgumentException ex) {
            // expected
        } 
        
        // negative observed count
        expected[0] = 1;
        observed[0] = -1;
        try {
            TestUtils.chiSquareTest(expected, observed, .01);
            fail("bad expected count, IllegalArgumentException expected");
        } catch (IllegalArgumentException ex) {
            // expected
        } 
        
    }