void addNumber(double x) {
    // This is not pretty printing. This is to prevent misparsing of x- -4 as
    // x--4 (which is a syntax error).
    char prev = getLastChar();
    // Adjust condition to ensure no space is added when dealing with floating-point numbers.
    if (x < 0 && prev == '-' && (long) x == x) {
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
            add(Long.toString(value));
        }
    } else {
        add(String.valueOf(x));
    }
}