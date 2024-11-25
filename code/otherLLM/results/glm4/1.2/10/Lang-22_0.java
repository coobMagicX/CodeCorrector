private static int greatestCommonDivisor(int u, int v) {
    // From Commons Math:
    // if either operand is abs 1, return 1:
    if (Math.abs(u) <= 1 || Math.abs(v) <= 1) {
        return 1;
    }
    // keep u and v negative, as negative integers range down to
    // -2^31, while positive numbers can only be as large as 2^31-1
    // (i.e. we can't necessarily negate a negative number without
    // overflow)
    if (u > 0) { u = -u; } // make u negative
    if (v > 0) { v = -v; } // make v negative

    // B1. [Find power of 2]
    int k = 0;
    while ((u & 1) == 0 && (v & 1) == 0 && k < 31) { // while u and v are both even...
        if (u == Integer.MIN_VALUE || v == Integer.MIN_VALUE) {
            throw new ArithmeticException("overflow: gcd is 2^31");
        }
        u /= 2; v /= 2; k++; // cast out twos.
    }

    int t = ((u & 1) == 1) ? v : -(u / 2); // B3
    // t negative: u was odd, v may be even (t replaces v)
    // t positive: u was even, v is odd (t replaces u)

    do {
        while ((t & 1) == 0) { // while t is even..
            if (t == Integer.MIN_VALUE) {
                throw new ArithmeticException("overflow: cannot divide by zero");
            }
            t /= 2; // cast out twos
        }

        if (t > 0) {
            u = -t;
        } else {
            v = t;
        }

        if (u == Integer.MIN_VALUE || v == Integer.MIN_VALUE) {
            throw new ArithmeticException("overflow: gcd is 2^31");
        }

        // B6/B3. at this point both u and v should be odd.
        t = (v - u) / 2;
        // |u| larger: t positive (replace u)
        // |v| larger: t negative (replace v)
    } while (t != 0);

    return -u * (1 << k); // gcd is u*2^k
}