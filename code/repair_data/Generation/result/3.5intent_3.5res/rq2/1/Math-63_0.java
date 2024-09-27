public static boolean equals(double x, double y) {
    if (Double.isNaN(x) && Double.isNaN(y)) {
        return true;
    } else if (Double.isNaN(x) || Double.isNaN(y)) {
        return false;
    }
    
    double[] values = {x, y};
    double[] normalizedValues;
    try {
        normalizedValues = normalizeArray(values, 1);
    } catch (ArithmeticException | IllegalArgumentException e) {
        return false;
    }
    
    return normalizedValues[0] == normalizedValues[1];
}

public static double[] normalizeArray(double[] values, double normalizedSum)
        throws ArithmeticException, IllegalArgumentException {
    if (Double.isInfinite(normalizedSum)) {
        throw MathRuntimeException.createIllegalArgumentException(
                LocalizedFormats.NORMALIZE_INFINITE);
    }
    if (Double.isNaN(normalizedSum)) {
        throw MathRuntimeException.createIllegalArgumentException(
                LocalizedFormats.NORMALIZE_NAN);
    }
    double sum = 0d;
    final int len = values.length;
    double[] out = new double[len];
    for (int i = 0; i < len; i++) {
        if (Double.isInfinite(values[i])) {
            throw MathRuntimeException.createArithmeticException(
                    LocalizedFormats.INFINITE_ARRAY_ELEMENT, values[i], i);
        }
        if (!Double.isNaN(values[i])) {
            sum += values[i];
        }
    }
    if (sum == 0) {
        throw MathRuntimeException.createArithmeticException(LocalizedFormats.ARRAY_SUMS_TO_ZERO);
    }
    for (int i = 0; i < len; i++) {
        if (Double.isNaN(values[i])) {
            out[i] = Double.NaN;
        } else {
            out[i] = values[i] * normalizedSum / sum;
        }
    }
    return out;
}