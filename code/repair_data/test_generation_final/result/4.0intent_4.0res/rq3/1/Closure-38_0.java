class NumberFormatter {
    private StringBuilder output = new StringBuilder();

    public void addNumber(double x) {
        // This is not pretty printing. This is to prevent misparsing of x - -4 as
        // x--4 (which is a syntax error).
        char prev = getLastChar();
        boolean negativeZero = isNegativeZero(x);
        if (x < 0 && prev == '-') {
            add(" ");
        }

        if ((long) x == x && !negativeZero) {
            long value = (long) x;
            long mantissa = value;
            int exp = 0;
            if (Math.abs(x) >= 100) {
                while (mantissa != 0 && value == mantissa * Math.pow(10, exp)) {
                    mantissa /= 10;
                    exp++;
                }
                exp--;  // Decrement once because we exit loop after one extra division
            }
            if (exp >= 2) {
                add(mantissa + "E" + exp);
            } else {
                add(Long.toString(value));
            }
        } else {
            add(String.valueOf(x));
        }
    }

    private char getLastChar() {
        if (output.length() > 0) {
            return output.charAt(output.length() - 1);
        }
        return ' '; // Default to space if no previous char
    }

    private boolean isNegativeZero(double x) {
        return Double.compare(x, -0.0) == 0;
    }

    private void add(String s) {
        output.append(s);
    }

    public String getOutput() {
        return output.toString();
    }
}