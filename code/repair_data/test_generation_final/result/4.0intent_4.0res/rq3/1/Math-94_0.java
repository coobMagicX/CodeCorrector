public static int gcd(int u, int v) {
    if (u == 0 || v == 0) {
        return Math.abs(u) + Math.abs(v);
    }
    // make u and v non-negative because negatives are handled automatically
    // by the steps in Euclid's algorithm
    u = Math.abs(u);
    v = Math.abs(v);

    // B1. [Find power of 2]
    int k = 0;
    while ((u & 1) == 0 && (v & 1) == 0) {
        u >>= 1;
        v >>= 1;
        k++;
    }

    // B2. Initialize: u and v have been divided by 2^k and at least
    // one is odd.
    while (u != 0) {
        // B3: Remove all factors of 2 in u
        while ((u & 1) == 0) {
            u >>= 1;
        }

        // B4: Remove all factors of 2 in v
        while ((v & 1) == 0) {
            v >>= 1;
        }

        // B5: Now both u and v are odd, subtract the smaller from the larger
        if (u >= v) {
            u = (u - v) >> 1;
        } else {
            v = (v - u) >> 1;
        }
    }
    
    return v << k; // gcd is v * 2^k
}