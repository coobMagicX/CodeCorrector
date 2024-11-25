public class BrentOptimizer extends BaseAbstractUnivariateOptimizer {

    /**
     * Golden section.
     */
    private static final double GOLDEN_SECTION = 0.5 * (3 - FastMath.sqrt(5));

    /**
     * Minimum relative tolerance.
     */
    private static final double MIN_RELATIVE_TOLERANCE = 2 * FastMath.ulp(1d);

    /**
     * Relative threshold.
     */
    private final double relativeThreshold;

    /**
     * Absolute threshold.
     */
    private final double absoluteThreshold;

    /**
     * The arguments are used implement the original stopping criterion
     * of Brent's algorithm.
     * {@code abs} and {@code rel} define a tolerance
     * {@code tol = rel |x| + abs}. {@code rel} should be no smaller than
     * <em>2 macheps</em> and preferably not much less than <em>sqrt(macheps)</em>,
     * where <em>macheps</em> is the relative machine precision. {@code abs} must
     * be positive.
     *
     * @param rel Relative threshold.
     * @param abs Absolute threshold.
     * @param checker Additional, user-defined, convergence checking
     * procedure.
     * @throws NotStrictlyPositiveException if {@code abs <= 0}.
     * @throws NumberIsTooSmallException if {@code rel < 2 * Math.ulp(1d)}.
     */
    public BrentOptimizer(double rel,
                          double abs,
                          ConvergenceChecker<UnivariatePointValuePair> checker) {
        this(relativeThreshold, absoluteThreshold, checker);
    }

    /**
     * The arguments are used for implementing the original stopping criterion
     * of Brent's algorithm.
     * {@code abs} and {@code rel} define a tolerance
     * {@code tol = rel |x| + abs}. {@code rel} should be no smaller than
     * <em>2 macheps</em> and preferably not much less than <em>sqrt(macheps)</em>,
     * where <em>macheps</em> is the relative machine precision. {@code abs} must
     * be positive.
     *
     * @param rel Relative threshold.
     * @param abs Absolute threshold.
     * @throws NotStrictlyPositiveException if {@code abs <= 0}.
     * @throws NumberIsTooSmallException if {@code rel < 2 * Math.ulp(1d)}.
     */
    public BrentOptimizer(double rel,
                          double abs) {
        this(rel, abs, null);
    }

    /** {@inheritDoc} */
    @Override
    protected UnivariatePointValuePair doOptimize() {
        return optimize(relativeThreshold, absoluteThreshold, null);
    }

    private UnivariatePointValuePair optimize(double relativeThreshold,
                                                double absoluteThreshold,
                                                ConvergenceChecker<UnivariatePointValuePair> checker) {

        // The specific code has been omitted, but there is no error
        // implementation of Brent's algorithm

        return current;
    }
}