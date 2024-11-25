protected double getInitialDomain(double p) {
    double d = getDenominatorDegreesOfFreedom();
    double numerator = d - 1; // Use the numerator degrees of freedom for this calculation
    double mean = (numerator > 0) ? d / numerator : Double.NaN; // Avoid division by zero

    if (Double.isNaN(mean)) {
        // If there is no valid mean, return a default value or handle accordingly
        return 1.0;
    } else {
        return mean; // Use the calculated mean as the initial domain
    }
}