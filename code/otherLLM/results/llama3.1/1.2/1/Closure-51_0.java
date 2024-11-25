void addNumber(double x) {
  // This is not pretty printing. This is to prevent misparsing of x- -4 as
  // x--4 (which is a syntax error).
  char prev = getLastChar();
  if (x < 0 && prev == '-') {
    append(" "); // Use append method for consistency
  }

  if ((long) x == x) { // Check for integer value
    long value = (long) x;
    long mantissa = value;
    int exp = 0;
    if (Math.abs(x) >= 100) { // Handle very large numbers
      while (mantissa / 10 * Math.pow(10, exp + 1) == value) {
        mantissa /= 10;
        exp++;
      }
    }
    if (exp > 2) { // Format number in scientific notation for very large numbers
      append(Long.toString(mantissa)); // Use append method
      append("E");
      append(Integer.toString(exp));
    } else {
      append(Long.toString(value));
    }
  } else { // Handle non-integer values, including zero and negative zero
    if (x == -0.0) { // Special case for negative zero
      append("-0"); // Format as "-0" instead of "-0.0"
    } else {
      append(String.valueOf(x));
    }
  }
}