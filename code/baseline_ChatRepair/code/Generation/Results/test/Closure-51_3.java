void addNumber(double x) {
  
  
  char prev = getLastChar();
  
  if (x < 0 && prev == '-') {
    add(" ");  
  }

  
  if (x == 0.0) {
    if (Double.doubleToLongBits(x) == Double.doubleToLongBits(-0.0)) {
      add("-0.0");  
    } else {
      add("0.0");  
    }
  } else if ((long) x == x) {
    
    long value = (long) x;
    long mantissa = value;
    int exp = 0;
    
    
    if (Math.abs(x) >= 100) {
      while (mantissa / 