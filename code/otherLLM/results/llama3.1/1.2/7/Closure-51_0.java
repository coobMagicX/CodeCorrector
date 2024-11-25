void addNumber(double x) {
  // This is not pretty printing. This is to prevent misparsing of x- -4 as
  // x--4 (which is a syntax error).
  char prev = getLastChar();
  if (x < 0 && prev == '-') {
    add(" ");
  }

  if ((long) x != x) { // Changed condition to check for decimal numbers
    String valueStr = String.valueOf(x);
    int exp = getExponent(valueStr); // Assuming a method exists to calculate the exponent
    if (exp > 2) {
      long mantissa = (long) Math.pow(10, exp - 1);
      add(Long.toString((long) (x * mantissa)) + "E" + Integer.toString(exp));
    } else {
      add(valueStr);
    }
  } else {
    long value = (long) x;
    long mantissa = value;
    int exp = getExponent(value); // Assuming a method exists to calculate the exponent
    if (exp > 2) {
      add(Long.toString(mantissa) + "E" + Integer.toString(exp));
    } else {
      add(Long.toString(value));
    }
  }

}