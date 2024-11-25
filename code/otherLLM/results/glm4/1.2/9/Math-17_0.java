public Dfp multiply(final int x) {
    // Check if the integer is zero, as multiplying by zero should always return zero.
    if (x == 0) {
        return new Dfp(0);
    }

    // Assuming there's a method to get and set the sign of the Dfp object.
    boolean negativeResult = false;
    if (this.isNegative()) {
        x = -x; // Flip the sign of the integer for multiplication
        negativeResult = !negativeResult;
    }

    // Assuming there's a method to get and set the magnitude of the Dfp object.
    long magnitudeLong = this.getMagnitude();
    int exponent = this.getExponent();

    // Handling integer overflow/underflow by checking bounds.
    if (magnitudeLong > Integer.MAX_VALUE || magnitudeLong < Integer.MIN_VALUE) {
        throw new ArithmeticException("Integer overflow or underflow occurred.");
    }

    // Convert the long magnitude to an int after safely ensuring it's within bounds.
    int magnitudeInt = (int) magnitudeLong;

    // Perform multiplication. The result is a Dfp object.
    Dfp result = new Dfp();
    try {
        result.setMagnitude(magnitudeInt * x);
    } catch (ArithmeticException e) {
        // Handle overflow/underflow by setting the maximum possible value or minimum negative value.
        if (negativeResult) {
            result.setSign(true); // Make it negative
            if (magnitudeInt > 0) {
                result.setMagnitude(Integer.MIN_VALUE);
            } else {
                result.setMagnitude(0); // If magnitude is already negative and we multiply by a positive, result is still negative.
            }
        } else {
            result.setSign(false); // Make it positive
            if (magnitudeInt < 0) {
                result.setMagnitude(Integer.MAX_VALUE);
            } else {
                result.setMagnitude(0); // If magnitude is already positive and we multiply by a negative, result is still positive.
            }
        }
    }

    // Update the exponent according to the rules of floating-point arithmetic for integers.
    int shift = 0;
    if (x < 0 && magnitudeInt == 0) {
        // Special case: multiplying zero by a negative number should be treated as zero, since -0 is valid.
        result.setMagnitude(0);
        exponent = 0;
    } else {
        shift = (int) Math.ceil(Math.log((double) Math.abs(x)) / Math.log(2));
        if (negativeResult && x < 0) {
            // If the original number was negative and we multiplied by a negative, adjust exponent accordingly.
            shift = -shift;
        }
        exponent += shift;
    }

    // Set the new exponent to the result object.
    result.setExponent(exponent);

    return result;
}