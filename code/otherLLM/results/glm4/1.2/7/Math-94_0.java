public static int gcd(int u, int v) {
    if (u * v == 0) {
        return Math.abs(u + v);
    }
    
    // keep u and v negative, as negative integers range down to
    // -2^31, while positive numbers can only be as large as 2^31-1
    // (i.e. we can't necessarily negate a negative number without
    // overflow)
    if (u > 0) {
        u = Integer.MIN_VALUE + 1 - u;
    } // make u negative without overflow
    if (v > 0) {
        v = Integer.MIN_VALUE + 1 - v;
    } // make v negative without overflow

    int k = 0;
    while ((u & 1) == 0 && (v & 1) == 0 && k < 31) { // while u and v are
                                                        // both even...
        u >>= 1; // use right shift instead of division to avoid overflow
        v >>= 1;
        k++; // cast out twos.
    }
    if (k == 31) {
        throw new ArithmeticException("overflow: gcd is 2^31");
    }

    int t = ((u & 1) == 1) ? v : -(u >> 1); // Use right shift instead of division
    do {
        while ((t & 1) == 0) { // while t is even..
            t >>= 1; // cast out twos using right shift to avoid overflow
        }
        if (t > 0) {
            u = Integer.MIN_VALUE + 1 - t;
        } else {
            v = t;
        }
        t = (v - u) >> 1; // Use right shift instead of division
    } while (t != 0);

    return u << k; // gcd is now always positive, so no need for the negative sign
}