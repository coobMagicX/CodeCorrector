    public void testExtremeValues() throws Exception {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);
        for (int i = 0; i < 100; i++) { // make sure no convergence exception
            double lowerTail = distribution.cumulativeProbability(-i);
            double upperTail = distribution.cumulativeProbability(i);
            if (i < 9) { // make sure not top-coded 
                // For i = 10, due to bad tail precision in erf (MATH-364), 1 is returned
                // TODO: once MATH-364 is resolved, replace 9 with 30
                assertTrue(lowerTail > 0.0d);
                assertTrue(upperTail < 1.0d);
            }
            else { // make sure top coding not reversed
                assertTrue(lowerTail < 0.00001);
                assertTrue(upperTail > 0.99999);
            }
        }
        
        assertEquals(distribution.cumulativeProbability(Double.MAX_VALUE), 1, 0);
        assertEquals(distribution.cumulativeProbability(-Double.MAX_VALUE), 0, 0);
        assertEquals(distribution.cumulativeProbability(Double.POSITIVE_INFINITY), 1, 0);
        assertEquals(distribution.cumulativeProbability(Double.NEGATIVE_INFINITY), 0, 0);
        
   }