public double[] fit() {
    final WeightedObservedPoint[] obs = getObservations();
    if (obs == null || obs.length < 3) {
        throw new NumberIsTooSmallException(obs == null ? 0 : obs.length, 3, true);
    }
    final double[] guess = (new ParameterGuesser(obs)).guess();
    return fit(new Gaussian.Parametric(), guess);
}