public static int gcd(int u, int v) {
    if (u == 0) return Math.abs(v);
    if (v == 0) return Math.abs(u);

    // keep u and v negative to avoid overflow when negating
    if (u > 0) u = -u;
    if (v > 0) v = -v;

    // B1. [Find power of 2]
    int k = 0;
    while ((u & 1) == 0 && (v & 1) == 0 && k < 31) { // while u and v are both even...
        u >>= 1;
        v >>= 1;
        k++; // cast out twos.
    }
    if (k == 31) {
        throw new ArithmeticException("overflow: gcd is 2^31");
    }

    // B2. Initialize: u and v have been divided by 2^k and at least one is odd.
    int t = ((u & 1) == 1) ? v : -(u >> 1); // B3
    // t negative: u was odd, v may be even (t replaces v)
    // t positive: u was even, v is odd (t replaces u)

    do {
        // B4/B3: cast out twos from t.
        while ((t & 1) == 0) { // while t is even..
            t >>= 1; // cast out twos
        }
        // B5 [reset max(u,v)]
        if (t > 0) {
            u = -t;
        } else {
            v = t;
        }
        // B6/B3. at this point both u and v should be odd.
        t = (v - u) >> 1;
        // |u| larger: t positive (replace u)
        // |v| larger: t negative (replace v)
    } while (t != 0);

    return -u * (1 << k); // gcd is u*2^k
}