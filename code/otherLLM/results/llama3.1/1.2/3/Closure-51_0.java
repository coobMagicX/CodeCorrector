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
    // Fix: Added a check to prevent adding extra characters when printing numbers
    if (exp > 2) {
      add(Long.toString(mantissa));
      add("E" + Integer.toString(exp));
    } else if (value != 0) { // Fix: Added this condition to avoid adding "E0"
      add(Long.toString(value));
    }
  } else {
    add(String.valueOf(x));
  }

}