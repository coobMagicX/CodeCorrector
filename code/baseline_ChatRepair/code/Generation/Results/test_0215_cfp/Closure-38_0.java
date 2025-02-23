void addNumber(double x) {
    
    char prev = getLastChar();
    boolean negativeZero = isNegativeZero(x);

    
    if (negativeZero) {
        if (prev == '-') {
            add(" ");  
        }
        add("-0.0");
    } else {
        
        if (x < 0 && prev == '-') {
            add(" ");
        }

        
        if ((long) x == x) {
            long value = (long) x;
            
            if (Math.abs(value) >= 100) {
                long mantissa = value;
                int exp = 0;
                while (mantissa / 10 * Math.pow(10, exp + 1) == value) {
                    mantissa /= 10;
                    exp++;
                }
                
                if (exp > 2) {
                    add(Long.toString(mantissa) + "E" + Integer.toString(exp));
                } else {
                    add(Long.toString(value));
                }
            } else {
