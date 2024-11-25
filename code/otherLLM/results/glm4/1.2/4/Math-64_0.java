public double[] numericalGradient(double[] x, Function f) {
    double h = 1e-5;
    double[] grad = new double[x.length];
    for (int i = 0; i < x.length; i++) {
        double[] xPerturbed = Arrays.copyOf(x, x.length);
        xPerturbed[i] += h;
        double dfPerturbed = f.evaluate(xPerturbed);
        grad[i] = (dfPerturbed - f.evaluate(x)) / h;
    }
    return grad;
}