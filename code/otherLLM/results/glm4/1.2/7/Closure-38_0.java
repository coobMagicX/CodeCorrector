void addNumber(double x) {
  // This is not pretty printing. This is to prevent misparsing of x- -4 as
  // x--4 (which is a syntax error).
  char prev = getLastChar();
  boolean negativeZero = isNegativeZero(x);
  
  if (x < 0 && prev == '-') {
    add(" "); // Ensure there's a space before adding a negative sign.
  }

  if ((long) x == x && !negativeZero) { // Only proceed if it's an integer and not a negative zero
    long value = (long) x;
    long mantissa = value;
    int exp = 0;

    if (Math.abs(x) >= 100) {
      while (mantissa / 10 * Math.pow(10, exp + 1) == value) { // Avoid overflow or unnecessary precision
        mantissa /= 10;
        exp++;
      }
    }

    if (exp > 2 || (x < 0 && mantissa % 10 != 0)) { // Check for scientific notation and ensure we handle negative zero correctly
      add(Long.toString(mantissa) + "E" + Integer.toString(exp));
    } else {
      add(Long.toString(value)); // Directly add the value if it's not in scientific notation or a negative zero
    }
  } else {
    add(String.valueOf(x)); // If negative zero, just convert to string and add.
  }
}