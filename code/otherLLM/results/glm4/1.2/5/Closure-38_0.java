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
    if (Math.abs(x) >= 100) {
      while (mantissa / 10 * Math.pow(10, exp + 1) == value) {
        mantissa /= 10;
        exp++;
      }
    }
    if (exp > 2) {
      add(Long.toString(mantissa) + "E" + Integer.toString(exp));
    } else {
      // Fixed the case for negative zero to print with a leading minus
      if (negativeZero && x < 0) {
        add("-");
      }
      add(Long.toString(value));
    }
  } else {
    // Fixed the case for negative zero to print with a leading minus
    if (negativeZero) {
      add("-");
    }
    add(String.valueOf(x));
  }
}