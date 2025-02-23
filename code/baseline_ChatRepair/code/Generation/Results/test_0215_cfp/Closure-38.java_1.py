void addNumber(double x) {
  char prev = getLastChar();
  boolean isNegativeZero = Double.compare(x, 0.0) == 0 && Double.doubleToRawLongBits(x) == Double.doubleToRawLongBits(-0.0);

  // Handle negative zero specifically
  if (isNegativeZero) {
    if (prev == '-') {
      add(" -0.0");
    } else {
      add("-0.0");
    }
    return;
  }

  // Handle space between minus signs
  if (x < 0 && prev == '-') {
    add(" ");
  }

  // Handling integer values that are exactly representable as a long
  if ((long) x == x && x != 0.0) {
    add(Long.toString((long) x));
  } else { // For floating point numbers that are not exactly representable as a long
    add(String.valueOf(x));
  }
}
