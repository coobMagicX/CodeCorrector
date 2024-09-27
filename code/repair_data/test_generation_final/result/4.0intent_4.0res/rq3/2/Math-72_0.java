import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.MaxIterationsExceededException;
import org.apache.commons.math.exception.MathRuntimeException;

public class RootFinder {

    private static final String NON_BRACKETING_MESSAGE = "Function values at endpoints do not have opposite signs. Endpoints: [%f, %f], Values: [%f, %f]";
    private double result;
    private double functionValueAccuracy = 1e-7;

    public double solve(final UnivariateRealFunction f, final double min, final double max, final double initial)
            throws MaxIterationsExceededException, FunctionEvaluationException {

        clearResult();
        verifySequence(min, initial, max);

        // return the initial guess if it is good enough
        double yInitial = f.value(initial);
        if (Math.abs(yInitial) <= functionValueAccuracy) {
            setResult(initial, 0);
            return result;
        }

        // return the first endpoint if it is good enough
        double yMin = f.value(min);
        if (Math.abs(yMin) <= functionValueAccuracy) {
            setResult(min, 0);
            return result;
        }

        // reduce interval if min and initial bracket the root
        if (yInitial * yMin < 0) {
            return solve(f, min, initial, min);
        }

        // return the second endpoint if it is good enough
        double yMax = f.value(max);
        if (Math.abs(yMax) <= functionValueAccuracy) {
            setResult(max, 0);
            return result;
        }

        // reduce interval if initial and max bracket the root
        if (yInitial * yMax < 0) {
            return solve(f, initial, max, initial);
        }

        if (yMin * yMax > 0) {
            throw MathRuntimeException.createIllegalArgumentException(
                    NON_BRACKETING_MESSAGE, min, max, yMin, yMax);
        }

        // full Brent algorithm starting with provided initial guess
        return solve(f, min, max, initial);
    }

    private void clearResult() {
        this.result = Double.NaN;
    }

    private void setResult(double value, int iterations) {
        this.result = value;
    }

    private void verifySequence(double min, double initial, double max) {
        if (min >= initial || initial >= max) {
            throw new IllegalArgumentException("Out of sequence: min < initial < max is not true.");
        }
    }
}