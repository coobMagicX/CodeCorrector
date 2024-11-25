void addNumber(double x) {
  // This is not pretty printing. This is to prevent misparsing of x- -4 as
  // x--4 (which is a syntax error).
  char prev = getLastChar();
  if ((x < 0 && prev == '-') || x == -0.0) { // Modified condition to handle edge case for negative zero
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
    if (exp > 2) { // Modified condition to ensure correct output for scientific notation
      add(Long.toString(mantissa));
      append("E");
      append(Integer.toString(exp));
    } else {
      add(Long.toString(value));
    }
  } else {
    add(String.valueOf(x));
  }

}