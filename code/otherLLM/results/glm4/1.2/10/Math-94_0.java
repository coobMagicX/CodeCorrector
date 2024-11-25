public static int gcd(int u, int v) {
    if (u == 0) return Math.abs(v);
    if (v == 0) return Math.abs(u);

    // Keep u and v negative to avoid overflow when negating.
    if (u > 0) {
        u = -u;
    }
    if (v > 0) {
        v = -v;
    }

    // B1. [Find power of 2]
    int k = 0;
    while ((u & 1) == 0 && (v & 1) == 0 && k < 31) { // while u and v are both even...
        u /= 2;
        v /= 2;
        k++; // cast out twos.
    }
    if (k >= 31) {
        throw new ArithmeticException("overflow: gcd is too large");
    }

    // B2. Initialize: u and v have been divided by 2^k and at least one is odd.
    int t = ((u & 1) == 1) ? v : -(u / 2);
    do {
        while ((t & 1) == 0) { // while t is even..
            t /= 2; // cast out twos
        }
        if (t > 0) {
            u = -t;
        } else {
            v = t;
        }
        t = (v - u) / 2;
    } while (t != 0);

    return u * (1 << k); // gcd is u*2^k
}