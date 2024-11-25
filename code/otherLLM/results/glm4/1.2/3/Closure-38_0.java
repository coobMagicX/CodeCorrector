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
      // Check for negative zero and format the string accordingly
      if (negativeZero) {
        add("-0");
      } else {
        add(Long.toString(value));
      }
    }
  } else {
    // Check for negative zero and format the string accordingly
    if (negativeZero) {
      add("-0");
    } else {
      add(String.valueOf(x));
    }
  }
}

static boolean isNegativeZero(double x) {
  return x == 0.0 && Math.copySign(1, x) == -1.0;
}