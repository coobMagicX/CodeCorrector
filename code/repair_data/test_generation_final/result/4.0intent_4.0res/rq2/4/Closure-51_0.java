void addNumber(double x) {
  // This is not pretty printing. This is to prevent misparsing of x- -4 as
  // x--4 (which is a syntax error).
  char prev = getLastChar();
  if (x < 0 && prev == '-') {
    add(" ");
  }

  // Check for negative zero (-0.0)
  if (x == 0.0 && 1/x == Double.NEGATIVE_INFINITY) {
    add("-0.0");
  } else if ((long) x == x) { // Check if x is effectively a long
    long value = (long) x;
    if (Math.abs(value) >= 100) {
      // Scientific notation conversion
      long mantissa = value;
      int exp = 0;
      while (mantissa / 10 * Math.pow(10, exp + 1) == value) {
        mantissa /= 10;
        exp++;
      }
      if (exp > 2) {
        add(Long.toString(mantissa) + "E" + Integer.toString(exp));
      } else {
        add(Long.toString(value));
      }
    } else {
      add(Long.toString(value));
    }
  } else {
    add(String.valueOf(x));
  }
}