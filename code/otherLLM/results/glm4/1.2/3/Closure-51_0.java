void addNumber(double x) {
  // Handle negative zero correctly by checking if the absolute value is zero
  if (x == 0 && Math.abs(x) != 0) {
    add("0");
    return;
  }

  char prev = getLastChar();
  if (x < 0 && prev == '-') {
    add(" ");
  }

  // Check for integer values by casting to long and comparing
  if ((long) x == x) {
    long value = (long) x;
    long mantissa = value;
    int exp = 0;

    // Handle numbers with absolute value greater than or equal to 100
    if (Math.abs(x) >= 100) {
      while (mantissa / 10 * Math.pow(10, exp + 1) == value) {
        mantissa /= 10;
        exp++;
      }
    }

    // Format the number according to the number of digits in the exponent
    if (exp > 2) {
      add(Long.toString(mantissa) + "E" + Integer.toString(exp));
    } else {
      add(Long.toString(value));
    }
  } else {
    add(String.valueOf(x)); // Handle non-integer values
  }

}