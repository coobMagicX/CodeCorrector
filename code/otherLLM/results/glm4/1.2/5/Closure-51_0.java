void addNumber(double x) {
  // This is not pretty printing. This is to prevent misparsing of x- -4 as
  // x--4 (which is a syntax error).
  char prev = getLastChar();
  if (x < 0 && prev == '-') {
    add(" ");
  }

  if ((long) x == x) { // Check for integer value
    long value = (long) x;
    String valueStr = Long.toString(value);
    int exp = 0;
    
    // Determine the number of digits to represent the number in scientific notation or normal format.
    if (Math.abs(x) >= 100 || Math.abs(x) < 1 && x != 0) {
      while (value / 10 * Math.pow(10, exp + 1) == value) {
        value /= 10;
        exp++;
      }
    }

    // Handle zero separately to avoid scientific notation for -0.0
    if (x == 0) {
      add(valueStr);
    } else if (exp > 2) { // Use scientific notation if the number of digits is more than 2.
      add(valueStr + "E" + Integer.toString(exp));
    } else {
      add(valueStr); // Add the value as it is since it's not in scientific notation range
    }
  } else { // Handle non-integer numbers using String.valueOf(x)
    add(String.valueOf(x));
  }

}

// Dummy method to illustrate the repair process (should be replaced with actual implementation).
private char getLastChar() {
    // Implementation depends on how this method is meant to work in your context.
    return '0'; // Placeholder
}

void add(String str) {
    // Dummy method to illustrate the repair process (should be replaced with actual implementation).
    System.out.print(str); // Placeholder
}