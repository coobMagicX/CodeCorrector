if (error <= 1.0) {
    // discrete events handling
    interpolator.storeTime(stepStart + stepSize);
    if (manager.evaluateStep(interpolator)) {
        final double dt = manager.getEventTime() - stepStart;
        if (Math.abs(dt) <= Math.ulp(stepStart)) {
            // rejecting the step would lead to a too small next step, we accept it
            loop = false;
        } else {
            // reject the step to match exactly the next switch time
            hNew = dt;
        }
    } else {
        // accept the step with some error tolerance
        loop = false;
        continue; // Continue with the outer loop
    }
} else {
    // reject the step and attempt to reduce error by stepsize control
    final double factor =
        Math.min(maxGrowth,
                 Math.max(minReduction, safety * Math.pow(error, exp)));
    hNew = filterStep(stepSize * factor, forward, false);
}