public double solve(final UnivariateRealFunction f, double min, double max, double initial)
throws MaxIterationsExceededException, FunctionEvaluationException {

    // Step 1: Initialize m to the midpoint of the interval if it is not within a predefined range around the initial guess
    double m = (min + max) / 2.0;
    // Adjust the midpoint if it's not within a certain distance from the initial value to ensure searching over the full range
    while (Math.abs(m - initial) < Math.abs(max - min) / 4.0) {
        if (m > initial) {
            max = m;
        } else {
            min = m;
        }
        m = UnivariateRealSolverUtils.midpoint(min, max);
    }

    clearResult();
    verifyInterval(min, max); // Step 2: Verify the interval
    double fm = f.value(m);
    double fmin = f.value(min);

    int i = 0;
    while (i < maximalIterationCount) {
        // Calculate midpoints within the current bracketing and check for convergence
        double midPoint = UnivariateRealSolverUtils.midpoint(min, max);
        double fMidPoint = f.value(midPoint);

        if (fm * fmin > 0.0) { // Root is in the upper half of the interval
            min = m;
            m = midPoint; // Update m to be the midpoint for the next iteration
            fm = fMidPoint; // Update fm to be the function value at the new midpoint
        } else {
            max = m; // Root is in the lower half of the interval
            m = midPoint; // Update m to be the midpoint for the next iteration
            fmin = fMidPoint; // Update fmin to be the function value at the new midpoint
        }

        if (Math.abs(max - min) <= absoluteAccuracy) { // Check for convergence
            setResult(m, i); // Set the result and return it
            return m;
        }
        ++i;

        // Step 3: Handle edge cases by checking if we've bracketed the root
        if ((fm > 0.0 && fMidPoint < 0.0) || (fm < 0.0 && fMidPoint > 0.0)) {
            break; // We have bracketed a root
        }
    }

    // Step 4: Check for accuracy
    if (Math.abs(max - min) <= absoluteAccuracy) {
        setResult(m, i);
        return m;
    }

    // Step 5: Throw exception if max iterations are exceeded or the function evaluation throws an exception
    throw new MaxIterationsExceededException(maximalIterationCount);
}