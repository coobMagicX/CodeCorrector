void addNumber(double x) {
    // This is not pretty printing. This is to prevent misparsing of x - -4 as
    // x--4 (which is a syntax error).
    char prev = getLastChar();
    if (x < 0 && prev == '-') {
        add(" ");
    }

    if ((long) x == x) {
        long value = (long) x;
        long mantissa = value;
        int exp = 0;
        if (Math.abs(value) >= 100) {
            while (mantissa >= 10 && value == mantissa * Math.pow(10, exp)) {
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