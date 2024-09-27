public static int gcd(int u, int v) {
    if (u == 0 || v == 0) {
        return Math.abs(u) + Math.abs(v);
    }

    // Ensure both u and v are positive
    u = Math.abs(u);
    v = Math.abs(v);

    // B1. [Find power of 2]
    int k = 0;
    while ((u & 1) == 0 && (v & 1) == 0) {
        u >>= 1;
        v >>= 1;
        k++;
    }

    // B2. Initialize: u and v have been divided by 2^k and at least one is odd.
    while (u != 0) {
        if ((u & 1) == 0) u >>= 1; // B3: Remove all factors of 2 in u
        else if ((v & 1) == 0) v >>= 1; // B4: Remove all factors of 2 in v
        else { // B5: Both u and v are odd, reduce larger argument
            if (u >= v)
                u = (u - v) >> 1;
            else
                v = (v - u) >> 1;
        }
    }
    return v << k; // gcd is v * 2^k
}