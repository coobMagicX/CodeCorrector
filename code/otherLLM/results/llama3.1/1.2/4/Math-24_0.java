public class CustomOptimizer extends BrentOptimizer {

    public CustomOptimizer(double rel, double abs) {
        super(rel, abs);
    }

    @Override
    protected double computeObjectiveValue(double x) {
        // Replace this with your actual function
        return Math.sin(x);
    }

    @Override
    protected ConvergenceChecker getConvergenceChecker() {
        // Replace this with your custom convergence checker if needed
        return null;
    }
}