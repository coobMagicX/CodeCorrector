void addNumber(double x) {
  // Check for negative zero value and handle it properly.
  if (x == -0.0 && !Double.isInfinite(x)) {
    add("0");
  } else {
    char prev = getLastChar();
    if (x < 0 && prev == '-') {
      add(" ");
    }

    if ((long) x == x) {
      long value = (long) x;
      long mantissa = value;
      int exp = 0;
      boolean isNegative = false;

      // Handling negative numbers by converting to positive
      // and setting a flag, then printing the sign later.
      if (value < 0) {
        isNegative = true;
        value = -value;
      }

      if (Math.abs(value) >= 100) {
        while (mantissa / 10 * Math.pow(10, exp + 1) == value) {
          mantissa /= 10;
          exp++;
        }
      }

      // Handling the case where we have a large number or many decimal places
      if (exp > 2 || x != Math.floor(x)) {
        add(Long.toString(mantissa));
        if (exp > 0) {
          add("E" + Integer.toString(exp));
        }
      } else {
        add(Long.toString(value));
      }

      // Add negative sign if necessary
      if (isNegative && mantissa == value) {
        add("-");
      }
    } else {
      add(String.valueOf(x));
    }
  }
}

// The rest of the methods remain unchanged.