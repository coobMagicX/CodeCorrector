public double[] fit() {
    final WeightedObservedPoint[] points = getObservations();
    if (points.length == 0) {
        throw new OutOfRangeException("No observations provided");
    }

    final double[] guess = basicGuess(points);
    return fit(new Gaussian.Parametric(), guess);
}