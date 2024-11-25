public class NormalDistributionImpl {

    public static double inverseCumulativeProbability(double p) {
        if (p < 0.0 || p > 1.0) {
            throw MathRuntimeException.createIllegalArgumentException(
                    "probability must be between 0 and 1: {0}", p);
        }

        // Assuming the use of a proper statistical library method for calculation
        double q = 1.0 - p; // Adjusting to use the inverse for the lower tail instead of upper tail
        double z = invNorm(q); // Inverse normal CDF, where 'q' is the adjusted probability
        return z;
    }

    private static double invNorm(double q) {
        if (q <= 0.5) {
            return normInv(q);
        } else {
            return -normInv(1.0 - q);
        }
    }

    // Placeholder for a statistical library's inverse normal CDF function
    // This should be replaced by an actual implementation from a reliable library like Apache Commons Math or JSci.
    private static double normInv(double p) {
        // Example: Using the normal inverse CDF function from a hypothetical statistical library
        // Note: In an actual application, replace this with a proper call to a statistical library method
        return 0.5 * Math.log(p / (1 - p)); // Simplified example, not accurate for actual use!
    }

    public static void main(String[] args) {
        double[] result = bracket(null, 0.5, -2.0, 2.0, 100);
        System.out.println("Bracketing result: [" + result[0] + ", " + result[1] + "]");
        double inverseCumulative = inverseCumulativeProbability(0.9772498680518209);
        System.out.println("Inverse cumulative probability: " + inverseCumulative);
    }

    public static double[] bracket(UnivariateRealFunction function,
            double initial, double lowerBound, double upperBound,
            int maximumIterations) throws ConvergenceException, FunctionEvaluationException {
        if (function == null) {
            throw MathRuntimeException.createIllegalArgumentException("function is null");
        }
        if (maximumIterations <= 0) {
            throw MathRuntimeException.createIllegalArgumentException(
                    "bad value for maximum iterations number: {0}", maximumIterations);
        }
        if (initial < lowerBound || initial > upperBound || lowerBound >= upperBound) {
            throw MathRuntimeException.createIllegalArgumentException(
                    "invalid bracketing parameters: lower bound={0}, initial={1}, upper bound={2}",
                    lowerBound, initial, upperBound);
        }
        double a = initial;
        double b = initial;
        double fa;
        double fb;
        int numIterations = 0;

        do {
            a = Math.max(a - 1.0, lowerBound);
            b = Math.min(b + 1.0, upperBound);
            fa = function.value(a);

            // Ensure that the second function value is evaluated
            if (Double.isNaN(fa)) {
                throw new FunctionEvaluationException("Function value at a is NaN", a);
            }

            fb = function.value(b);
            numIterations++;
        } while ((fa * fb > 0.0) && (numIterations < maximumIterations) &&
                ((a > lowerBound) || (b < upperBound)));

        if (fa * fb >= 0.0) {
            throw new ConvergenceException(
                    "number of iterations={0}, maximum iterations={1}, initial={2}, lower bound={3}, " +
                            "upper bound={4}, final a value={5}, final b value={6}, f(a)={7}, f(b)={8}",
                    numIterations, maximumIterations, initial,
                    lowerBound, upperBound, a, b, fa, fb);
        }

        return new double[]{a, b};
    }
}