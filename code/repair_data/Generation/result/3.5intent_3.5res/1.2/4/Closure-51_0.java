void addNumber(double x) {
  // This is not pretty printing. This is to prevent misparsing of x- -4 as
  // x--4 (which is a syntax error).
  char prev = getLastChar();
  if (x == 0.0 && 1/x == Double.NEGATIVE_INFINITY) {
    add("-0.0");
  } else if (x < 0 && prev == '-') {
    add(" ");
  } else if (Double.isNaN(x)) {
    add("NaN");
  } else if (Double.isInfinite(x)) {
    if (x > 0) {
      add("Infinity");
    } else {
      add("-Infinity");
    }
  } else if ((long) x == x) {
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