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
    // Handle the special case for negative zero
    if (negativeZero && x == Double.NaN) {
      add("-0");
    } else {
      if (exp > 2) {
        add(Long.toString(mantissa) + "E" + Integer.toString(exp));
      } else {
        add(Long.toString(value));
      }
    }
  } else {
    add(String.valueOf(x));
  }
}

// The rest of the methods remain unchanged
static boolean isNegativeZero(double x) {
  return x == 0.0 && Math.copySign(1, x) == -1.0;
}
abstract char getLastChar();

void addIdentifier(String identifier) {
  add(identifier);
}