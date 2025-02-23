public double evaluate(final double[] values, final double[] weights,
                       final double mean, final int begin, final int length) {

    double var = Double.NaN;

    if (test(values, weights, begin, length)) {
        if (length == 1) {
            var = 0.0; // Variance is 0 if there's only one element.
        } else if (length > 1) {
            double accum = 0.0; // Accumulator for the squared deviations
            double dev = 0.0; // Deviation from the mean
            double accum2 = 0.0; // Accumulator for the deviations
            double sumWts = 0.0; // Sum of weights

            for (int i = begin; i < begin + length; i++) {
                dev = values[i] - mean; // Deviation of value from mean
                accum += weights[i] * dev * dev; // Weighted sum of squared deviation
                accum2 += weights[i] * dev; // Weighted sum of deviation
                sumWts += weights[i]; // Total weight sum for the segment
            }

            if (isBiasCorrected) {
                var = (accum - (accum2 * accum2 / sumWts)) / (sumWts - 1.0); // Bias-corrected variance
            } else {
                var = (accum - (accum2 * accum2 / sumWts)) / sumWts; // Variance
            }
        }
    }
    return var;
}
