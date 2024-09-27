void addNumber(double x) {
    // This is not pretty printing. This is to prevent misparsing of x- -4 as
    // x--4 (which is a syntax error).
    char prev = getLastChar();
    boolean negativeZero = isNegativeZero(x);
    if (x < 0 && prev == '-') {
        add(" ");
    }

    if (x == 0.0 && negativeZero) {
        add("-0");
    } else if ((long) x == x && !negativeZero) {
        long value = (long) x;
        long mantissa = value;
        int exp = 0;
        if (Math.abs(x) >= 100) {
            while (mantissa % 10 == 0) {
                mantissa /= 10;
                exp++;
            }
        }
        if (exp > 2) {
            add(Long.toString(mantissa) + "E" + Integer.toString(exp));
        } else {
            add(Long.toString(value));
        }
    } else {
        add(String.valueOf(x));
    }
}