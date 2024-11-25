void addNumber(double x) {
  // Ensure proper spacing around negative signs for doubles.
  if (x < 0 && getLastChar() != '-') {
    add(" ");
    add("-");
  } else if (getLastChar() == '-') {
    removeLast(); // Remove the last character if it's an unnecessary dash
  }

  if ((long) x == x) {
    long value = (long) x;
    long mantissa = value;
    int exp = 0;

    // Handle very large or very small doubles correctly.
    double absValue = Math.abs(x);
    if (absValue >= 100 || absValue < 1.0) {
      while (mantissa / 10 * Math.pow(10, exp + 1) == value && mantissa != 0) {
        mantissa /= 10;
        exp++;
      }
    }

    // Format the number as an integer or in scientific notation depending on the exponent.
    if (exp > 2 || absValue < 1.0) { // Use scientific notation for large numbers or very small ones
      add(Long.toString(mantissa));
      if (x != mantissa) { // Only add the exponent part if it contributes to the representation
        add("E");
        add(Integer.toString(exp));
      }
    } else {
      add(Long.toString(value));
    }
  } else {
    add(String.valueOf(x));
  }
}