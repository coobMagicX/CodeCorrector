public static int gcd(int u, int v) {
    if (u == 0 || v == 0) {
        return Math.abs(u + v);
    }

    // Make both u and v non-negative
    u = Math.abs(u);
    v = Math.abs(v);

    // Remove common factors of 2
    int shift;
    for (shift = 0; ((u | v) & 1) == 0; ++shift) {
        u >>= 1;
        v >>= 1;
    }

    // Dividing u by 2 until u becomes odd
    while ((u & 1) == 0) {
        u >>= 1;
    }

    // From here on, one of u and v is always even, the other is odd
    do {
        // Removing factors of 2 in v, because v is even
        while ((v & 1) == 0) {
            v >>= 1;
        }

        // Now u and v are both odd, ensuring u <= v
        if (u > v) {
            int temp = u;
            u = v;
            v = temp;
        }

        // v becomes v - u (which is even) and can be divided by 2
        v = (v - u) >> 1;

    } while (v != 0);

    return u << shift; // gcd is the remaining 'u' adjusted by the common factors of 2 that we extracted
}
