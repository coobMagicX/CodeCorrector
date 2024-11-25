void addNumber(double x) {
  // This is not pretty printing. This is to prevent misparsing of x- -4 as
  // x--4 (which is a syntax error).
  char prev = getLastChar();
  if (x < 0 && prev == '-') {
    add(" ");
  }

  if ((long) x == x) {
    long value = (long) x;
    long mantissa = Math.abs(value);
    int exp = 0;

    // Handle zero explicitly, to avoid unnecessary formatting
    if (value == 0) {
      add("0");
    } else {
      // Check for scientific notation condition
      if (Math.abs(x) >= 100 || mantissa > 9) { // mantissa > 9 implies more than two digits
        while (mantissa / 10 * Math.pow(10, exp + 1) == value) {
          mantissa /= 10;
          exp++;
        }
        add(Long.toString(mantissa) + "E" + Integer.toString(exp));
      } else {
        // No scientific notation needed for single or double digit numbers
        add(Long.toString(value));
      }
    }
  } else {
    // For non-integer doubles, convert to string without adding extra space before negative sign
    add(String.valueOf(x));
  }

}