public double evaluate(final double[] values, final double[] weights, final double mean, final int begin, final int length) {
    double var = Double.NaN;

    if (test(values, weights, begin, length)) {
        if (length == 1) {
            var = 0.0;
        } else if (length > 1) {
            double sumWts = 0.0; 
            double accum = 0.0; 
            double accum2 = 0.0; 

            
            for (int i = begin; i < begin + length; i++) {
                double dev = values[i] - mean; 
                double wtDev = weights[i] * dev; 
                accum += wtDev * dev; 
                sumWts += weights[i]; 
                accum2 += wtDev; 
            }

            
            double meanDeviationSquared = (accum2 * accum2) / sumWts;
            if (isBiasCorrected && sumWts > 1.0) {
                var = (accum - meanDeviationSquared) / (sumWts - 1.0);
            } else {
                var = (accum - meanDeviationSquared) / sumWts;
            }
        }
    }
    return var;
}
