void addNumber(double x) {
  // This is not pretty printing. This is to prevent misparsing of x- -4 as
  // x--4 (which is a syntax error).
  char prev = getLastChar();
  boolean negativeZero = isNegativeZero(x);
  
  // Adjust for spacing only if x is negative or a negative zero after a minus 
  if ((x < 0 || negativeZero) && prev == '-') {
    add(" ");
  }

  // Separate handling for when x is an integer value vs when it is not.
  // This avoids unnecessary decimal places or changes in notation.
  if ((long) x == x && !negativeZero) {
    add(Long.toString((long) x));  // Cast to long removes any decimal point
  } else {
    // Use standard floating-point representation for non-integers and negative zero
    add(String.valueOf(x));
  }
}
