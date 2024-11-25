public double[] fit() {
    try {
        final double[] guess = (new ParameterGuesser(getObservations())).guess();
        if (guess == null || guess.length != 2) {
            throw new IllegalArgumentException("Guess array must have exactly two elements for mean and sigma.");
        }
        return fit(new Gaussian.Parametric(), guess);
    } catch (Exception e) {
        // Log the exception or handle it appropriately
        System.err.println("Error during fitting: " + e.getMessage());
        // Return a default value or throw an appropriate custom exception
        return new double[]{Double.NaN, Double.NaN};
    }
}