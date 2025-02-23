public double getNumericalMean() {
    return ((double) getSampleSize() * (double) getNumberOfSuccesses()) / (double) getPopulationSize();
}
