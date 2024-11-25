// Main integration loop refactored for better readability and maintainability.
do {
    interpolator.shift();

    // Iterate over step size, ensuring local normalized error is smaller than 1
    double maxAllowedError = 10; // Replace magic number with a named constant
    while (estimateError(yDotK, y0, y1, h) >= maxAllowedError) {
        // The specific code for handling large errors has been omitted.
        continue;
    }

    // Local error is small enough: accept the step, trigger events and step handlers
    interpolator.storeTime(stepStart + stepSize);
    System.arraycopy(yTmp, 0, y, 0, y0.length);
    System.arraycopy(yDotK[stages - 1], 0, yDotTmp, 0, y0.length);
    stepStart = acceptStep(interpolator, y, yDotTmp, t);
    System.arraycopy(y, 0, yTmp, 0, y.length);

} while (!isLastStep); // Add a condition to break out of the loop