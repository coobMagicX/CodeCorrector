void addNumber(double x) {
  // Capture the last character for context-sensitive output (like after a minus sign).
  char prev = getLastChar();
  boolean negativeZero = isNegativeZero(x);
  
  // Conditionally add space only if dealing with negative zero directly after a minus sign.
  if (negativeZero && prev == '-') {
    add(" ");
  }

  // Handle the case where the number is exactly representable as a long integer
  // and is not negative zero
  if ((long) x == x && !negativeZero) {
    long value = (long) x;
    long mantissa = value;
    int exp = 0;
    
    // Determine if scientific notation is needed
    if (Math.abs(x) >= 100) {
      while (mantissa / 10 * Math.pow(10, exp + 1) == value) {
        mantissa /= 10;
        exp++;
      }
    }
    
    // Apply scientific notation formatting if needed
    if (exp > 2) {
      add(Long.toString(mantissa) + "E" + Integer.toString(exp));
    } else {
      add(Long.toString(value));
    }
  } else {
    // For all other floating point numbers, use standard decimal notation
    add(String.valueOf(x));
  }
}
