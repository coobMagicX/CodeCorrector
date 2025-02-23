void addNumber(double x) {
    
    char prev = getLastChar();
    boolean negativeZero = isNegativeZero(x);

    
    if (negativeZero) {
        if (prev == '-') {
            add(" "); 
        }
        add("-0.0");
        return; 
    }

    
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
        if (exp > 2) {
            add(Long.toString(mantissa) + "E" + Integer.toString(exp)); 
        } else {
            add