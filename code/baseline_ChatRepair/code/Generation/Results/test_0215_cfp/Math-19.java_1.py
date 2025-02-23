// Check for mixed finite and infinite bounds which are unsupported
if (hasFiniteBounds && hasInfiniteBounds) {
    throw new MathUnsupportedOperationException();
}
