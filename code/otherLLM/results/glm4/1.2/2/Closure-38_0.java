void addNumber(double x) {
  // This is not pretty printing. This is to prevent misparsing of x- -4 as
  // x--4 (which is a syntax error).
  char prev = getLastChar();
  boolean negativeZero = isNegativeZero(x);
  
  if (x < 0 && prev == '-') {
    add(" ");
  }

  if ((long) x == x && !negativeZero) {
    long value = (long) x;
    long mantissa = value;
    int exp = 0;

    // Adjusted the condition to handle numbers greater than or equal to 100
    while (Math.abs(mantissa) >= 10 && Math.abs(mantissa) / 10 * Math.pow(10, exp + 1) == value) {
      mantissa /= 10;
      if (mantissa < 0) { // If the mantissa becomes negative, adjust the exponent
        exp--;
      } else {
        exp++;
      }
    }

    if (exp > 2) {
      add(Long.toString(mantissa) + "E" + Integer.toString(exp));
    } else {
      add(Long.toString(value));
    }
  } else {
    // Check for negative zero before calling String.valueOf to avoid incorrect formatting
    if (!negativeZero) {
      add(String.valueOf(x));
    } else {
      // Correctly format negative zero as "-0.0"
      add("-0.0");
    }
  }
}