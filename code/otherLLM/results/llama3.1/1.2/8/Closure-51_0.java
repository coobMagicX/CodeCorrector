void addNumber(double x) {
  // This is not pretty printing. This is to prevent misparsing of x- -4 as
  // x--4 (which is a syntax error).
  char prev = getLastChar();
  if (x < 0 && prev == '-') {
    add(" ");
  }

  if ((long) x == x) {
    long value = (long) x;
    long mantissa = value;
    int exp = 0;
    if (Math.abs(x) >= 100) {
      while (mantissa / 10 * Math.pow(10, exp + 1) == value) {
        mantissa /= 10;
        exp++;
      }
    }

    // Check for very small numbers close to zero
    if (exp > -14 && x != 0) { 
      add(Long.toString(mantissa) + "E" + Integer.toString(exp));
    } else if (x == 0) {
      // Handle negative zeros
      if (x < 0) {
        add("-0.0");
      } else {
        add("0.0");
      }
    } else {
      add(Long.toString(value));
    }
  } else {
    add(String.valueOf(x));
  }

}