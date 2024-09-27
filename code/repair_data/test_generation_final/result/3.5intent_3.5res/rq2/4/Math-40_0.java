final double xTol = getAbsoluteAccuracy() +
                    getRelativeAccuracy() * FastMath.max(FastMath.abs(xA), FastMath.abs(xB));
if (((xB - xA) <= xTol) || (FastMath.max(absYA, absYB) < getFunctionValueAccuracy())) {
    // ...
}