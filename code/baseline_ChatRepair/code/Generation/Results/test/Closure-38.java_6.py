void addNumber(double x) {
  // This is not pretty printing. This is to prevent misparsing of x - -4 as
  // x--4 (which is a syntax error).
  char prev = getLastChar();
  boolean negativeZero = isNegativeZero(x);
  if (x == 0.0 && Double.doubleToRawLongBits(x) < 0) { // Check for negative zero
    add("-0.0");
  } else if (x < 0 && prev == '-') {
    add(" "); // Add space to separate double negative signs
    add(String.valueOf(x));
  } else if ((long) x == x && !negativeZero) {
    long value = (long) x;
    long mantissa = value;
    int exp = 0;
    if (Math.abs(x) >= 100) {
      while (mantissa / 10 * Math.pow(10, exp + 1) == value) {
        mantissa /= 10;
        exp++;
      }
    }
    if (exp > 2) {
      add(Long.toString(mantissa) + "E" + Integer.toString(exp));
    } else {
      add(Long.toString(value));
    }
  } else {
    add(String.valueOf(x));
  }
}

boolean isNegativeZero(double x) {
  return Double.doubleToRawLongBits(x) == Double.doubleToRawLongBits(-0.0);
}
