void addNumber(double x) {
  // This is not pretty printing. This is to prevent misparsing of x- -4 as
  // x--4 (which is a syntax error).
  char prev = getLastChar();
  boolean negativeZero = isNegativeZero(x);
  
  if (x < 0 && prev == '-') {
    add(" ");
  }

  if ((long) x == x && !negativeZero) {
    long value = (long) x;
    long mantissa = value;
    int exp = 0;
    
    // Corrected condition for scientific notation
    if (Math.abs(x) >= Math.pow(10, 2)) { 
      while (mantissa / 10 * Math.pow(10, exp + 1) == value) {
        mantissa /= 10;
        exp++;
      }
    }

    if (exp > 2 || (x < 0 && isNegativeZero(x))) { // Corrected condition for negative zero
      add(Long.toString(mantissa) + "E" + Integer.toString(exp));
    } else {
      add(Long.toString(value));
    }
  } else {
    add(String.valueOf(x));
  }
}