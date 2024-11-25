public class Example {
    public static void main(String[] args) {
        Function f = x -> x * x - 4; // An example function to find roots.
        double root = brentMethod(f, 0.0, 2.0); // Finds a root in the interval [0, 2].
        System.out.println("Root found: " + root);
    }

    public static double brentMethod(Function f, double a, double b) {
        return doBrentMethod(f, a, b).x;
    }
    
    private static UnivariatePointValuePair doBrentMethod(Function f, double a, double b) {
        // This method is the implementation of the actual Brent's method.
        // (The code from the question snippet should be placed here.)
    }

    interface Function {
        double value(double x);
    }
}