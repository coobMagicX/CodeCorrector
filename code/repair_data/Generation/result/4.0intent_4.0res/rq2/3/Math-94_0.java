public static int gcd(int u, int v) {
    if (u == 0 || v == 0) {
        return Math.abs(u) + Math.abs(v);
    }
    // make u and v non-negative
    u = Math.abs(u);
    v = Math.abs(v);
    
    // B1. [Find power of 2]
    int k = 0;
    while ((u & 1) == 0 && (v & 1) == 0) { // while u and v are both even...
        u >>= 1; // divide u by 2
        v >>= 1; // divide v by 2
        k++; // increase k since gcd will have 2^k as factor
    }

    // B2. Initialize: make sure u is odd if one must be odd
    if ((u & 1) == 0) {
        int temp = u;
        u = v;
        v = temp;
    }

    // B3. Make v negative if it's odd, so subtraction in the loop simplifies
    if ((v & 1) != 0) {
        v = -v;
    }

    // Core loop of the algorithm
    while (v != 0) {
        // B4: Make v even by removing all factors of 2
        while ((v & 1) == 0) {
            v >>= 1; // divide v by 2
        }

        // B5: Reset max(u, |v|)
        if (u > -v) {
            v = -(v + u);
        } else {
            u = -(v + u);
        }

        // B6: Ensure u is non-negative
        u = Math.abs(u);
    }
    return u << k; // gcd is u*2^k
}