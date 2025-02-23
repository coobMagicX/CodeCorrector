void addNumber(double x) {
  
  
  char prev = getLastChar();
  if (x < 0 && prev == '-') {
    add(" ");
  }

  if (x == 0.0 && Double.compare(x, -0.0) == 0) {
    
    add("-0.0");
  } else if ((long) x == x) {
    long value = (long) x;
    long mantissa = value;
    int exp = 0;
    if (Math.abs(value) >= 100) {
      while (mantissa / 10 * Math.pow(10, exp + 1) == value) {
        mantissa /= 10;
        exp++;
      }
    }
    if (exp > 2) {
      add(mantissa + "E" + exp);
    } else {
      add(Long.toString(value));
    }
  } else {
    add(String.valueOf(x));
  }
}
