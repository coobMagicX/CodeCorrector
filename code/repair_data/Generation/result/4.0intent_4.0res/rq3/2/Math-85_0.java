import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;

public static double[] bracket(UnivariateFunction function,
                               double initial, double lowerBound, double upperBound,
                               int maximumIterations) throws ConvergenceException, MathIllegalArgumentException {

    if (function == null) {
        throw new MathIllegalArgumentException(LocalizedFormats.NULL_FUNCTION);
    }
    if (maximumIterations <= 0) {
        throw new MathIllegalArgumentException(LocalizedFormats.INVALID_MAX_ITERATIONS, maximumIterations);
    }
    if (initial < lowerBound || initial > upperBound || lowerBound >= upperBound) {
        throw new MathIllegalArgumentException(LocalizedFormats.INVALID_BRACKETING_PARAMETERS,
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
        fb = function.value(b);
        numIterations++;
    } while ((fa * fb > 0.0) && (numIterations < maximumIterations) &&
            ((a > lowerBound) || (b < upperBound)));

    if (fa * fb >= 0.0) {
        throw new ConvergenceException(LocalizedFormats.FAILED_BRACKETING,
                                      numIterations, maximumIterations, initial,
                                      lowerBound, upperBound, a, b, fa, fb);
    }

    return new double[]{a, b};
}