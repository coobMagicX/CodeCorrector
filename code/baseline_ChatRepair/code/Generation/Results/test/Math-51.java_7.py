// Calculate the next approximation.
double x = x1 - ((f1 * (x1 - x0)) / (f1 - f0));

// Check for small denominators
if (Math.abs(f1 - f0) < SMALL_EPSILON) {
    x = 0.5 * (x1 + x0); // simply choose the midpoint if the function values are too close
}

final double fx = computeObjectiveValue(x);

// Proper update of bounds
if (fx == 0.0) {
    return x; // Found exact root
}

if (f1 * fx < 0) {
    x0 = x1;
    f0 = f1;
} else {
    if (f0 * fx < 0) {
        x1 = x;
        f1 = fx;
    }
    else {
        // Both f0 and fx have the same signs: no bracketing, choose new x
        x = 0.5 * (x1 + x0);
        fx = computeObjectiveValue(x);
    }
}

// Switch sides
x0 = x;
f0 = fx;
