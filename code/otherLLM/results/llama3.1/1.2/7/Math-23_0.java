public class Example {
    public static void main(String[] args) {
        // Define your objective function
        double f(double x) {
            return Math.pow(x, 2); // For demonstration purposes, a simple quadratic function.
        }

        // Create an instance of the ConvergenceChecker if needed
        ConvergenceChecker<UnivariatePointValuePair> checker = new MyConvergenceChecker();

        // Call the doOptimization method to find the minimum of your objective function within a given range
        UnivariatePointValuePair result = doOptimization(f, 1.0, -10, 10, checker);

        System.out.println("Minimum found at x = " + result.getPoint());
    }
}