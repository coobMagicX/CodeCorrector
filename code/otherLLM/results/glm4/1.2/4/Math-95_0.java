protected double getInitialDomain(double p) {
    double d = getDenominatorDegreesOfFreedom();
    double mean = d; // Assuming the method returns the mean, which is common for FDistributionImpl

    // Adjust calculation to use inverse cumulative probability (assuming this is an error in the original calculation)
    ret = 1.0 - (p * (d - 2.0) / d); // This formula assumes that p represents the cumulative probability

    return ret;
}