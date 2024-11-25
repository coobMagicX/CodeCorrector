public class Optimizer {
    private double startValue;
    private double min;
    private double max;

    public Optimizer(double startValue, double min, double max) {
        this.startValue = startValue;
        this.min = min;
        this.max = max;
    }

    public double getStartValue() {
        return startValue;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public ConvergenceChecker<UnivariatePointValuePair> getConvergenceChecker() {
        // Return an instance of ConvergenceChecker
    }

    public boolean getMinimize() {
        // Return whether to minimize or maximize the objective function
    }
}