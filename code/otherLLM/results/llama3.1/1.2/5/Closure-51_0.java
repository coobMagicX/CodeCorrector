void addNumber(double x) {
  // This is not pretty printing. This is to prevent misparsing of x- -4 as
  // x--4 (which is a syntax error).
  char prev = getLastChar();
  if (x < 0 && prev == '-') {
    add(" ");
  }

  if ((long) x == x) { // check for integer values
    long value = (long) x;
    long mantissa = value;
    int exp = 0;
    if (Math.abs(x) >= 10000.0) { // handle large numbers correctly
      while (mantissa / 10 * Math.pow(10, exp + 1) == value) {
        mantissa /= 10;
        exp++;
      }
    } else if (Math.abs(x) < 1e-4 && Math.abs(x) >= 1000.0) { // handle very small numbers correctly
      while (mantissa / 10 * Math.pow(10, exp - 1) == value) {
        mantissa *= 10;
        exp--;
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