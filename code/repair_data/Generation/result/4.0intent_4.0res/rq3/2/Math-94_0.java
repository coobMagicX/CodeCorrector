public static int gcd(int u, int v) {
    if (u == 0 || v == 0) {
        return Math.abs(u) + Math.abs(v);
    }

    // Make both u and v non-positive to handle range issues
    u = Math.abs(u);
    v = Math.abs(v);

    // B1. [Find power of 2]
    int k = 0;
    while ((u & 1) == 0 && (v & 1) == 0) { // while u and v are both even...
        u >>= 1;
        v >>= 1;
        k++; // cast out twos.
    }

    // B2. Initialize: u and v have been divided by 2^k and at least one is odd.
    int t = (u & 1) == 1 ? v : -u / 2;

    do {
        // B4/B3: cast out twos from t.
        while ((t & 1) == 0) { // while t is even..
            t >>= 1; // cast out twos
        }

        // B5 [reset max(u, v)]
        if (t > 0) {
            u = t;
        } else {
            v = -t;
        }

        // B6/B3. At this point both u and v should be odd.
        t = (v - u) / 2;
        // |u| larger: t positive (replace u)
        // |v| larger: t negative (replace v)
    } while (t != 0);

    return u * (1 << k); // gcd is u*2^k
}