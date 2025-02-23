void addNumber(double x) {
  char prev = getLastChar();
  if (x == 0.0 && Double.doubleToRawLongBits(x) == Double.doubleToRawLongBits(-0.0)) {
    if (prev == '-') {
      add(" -0.0"); // Ensure space and correct negative zero formatting
      return;
    }
    add("-0.0");
    return;
  }

  if (x == 0.0) {
    add("0.0");
    return;
  }
  
  if ((long) x == x && x != 0.0) {
    long value = (long) x;
    long mantissa = value;
    int exp = 0;

    if (Math.abs(x) >= 100) {
      while (mantissa / 10 * Math.pow(10, exp + 1) == value) {
        mantissa /= 10;
        exp++;
      }
    } 

    if (exp > 2) {
      add(Long.toString(mantissa) + "E" + Integer.toString(exp));
    } else {
      if (x < 0 && prev == '-') {
        add(" ");
      }
      add(Long.toString(value));
    }
  } else {
    if (x < 0 && prev == '-') {
      add(" ");
    }
    add(String.valueOf(x));
  }
}
