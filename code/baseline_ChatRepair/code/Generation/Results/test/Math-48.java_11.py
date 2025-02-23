// Update the bounds with the new approximation.
if (f1 * fx < 0) {
    x0 = x1;
    f0 = f1;
    inverted = !inverted;
} else if (fx * f0 < 0) {
    // Normally x would become x1, but if f1 didn't change sign, check if f0 changed sign with fx
    x1 = x;
    f1 = fx;
    inverted = !inverted;
} else {
    // Neither bounds update could lead to a sign change which indicates
    // potential issues like local extremum between x0 and x1 without crossing x-axis.
    // Reduce the interval "conservatively" to prevent looping over the same interval.
    x1 = x;
    f1 = fx;
}

// Regular update of x1 to x and f1 to fx if the above special cases did not execute.
if (x0 != x1) {
    x1 = x;
    f1 = fx;
}
