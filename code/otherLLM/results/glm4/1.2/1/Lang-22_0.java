private static int greatestCommonDivisor(int u, int v) {
    if (Math.abs(u) <= 1 || Math.abs(v) <= 1) {
        return 1;
    }
    if (u > 0) { u = -u; } // make u negative
    if (v > 0) { v = -v; } // make v negative

    int k = 0;
    while ((u & 1) == 0 && (v & 1) == 0 && k < 31) {
        u /= 2;
        v /= 2;
        k++;
    }
    if (k == 31) {
        throw new ArithmeticException("overflow: gcd is 2^31");
    }

    int t = ((u & 1) == 1) ? v : -(u / 2);
    do {
        while ((t & 1) == 0) {
            t /= 2;
        }
        if (t > 0) {
            u = -t;
        } else {
            v = t;
        }

        // Ensuring that t is always odd before the subtraction
        t = (v - u) / 2;
    } while (t != 0);

    // Avoiding overflow by not using bit shifting for a large k.
    int gcd = u;
    if (k > 0) {
        if (u < 0) {
            gcd = -gcd;
        }
        for (int i = 1; i < k; i++) {
            if (gcd == Integer.MIN_VALUE) {
                throw new ArithmeticException("overflow: gcd is Integer.MIN_VALUE");
            }
            gcd *= 2;
        }
    }
    return gcd;
}