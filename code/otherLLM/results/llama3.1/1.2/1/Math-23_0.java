public class Main {
    public static void main(String[] args) {
        // Define a function f(x) = x^2 - 4x + 3
        double f(double x) {
            return Math.pow(x, 2) - 4*x + 3;
        }

        // Create an instance of BrentOptimizer with default tolerances and convergence checker
        BrentOptimizer optimizer = new BrentOptimizer();

        // Perform the optimization process
        UnivariatePointValuePair result = optimizer.doOptimize(f);

        System.out.println("Root of f(x) found at x = " + result.getPoint());
    }
}