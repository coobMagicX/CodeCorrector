public static int gcd(int u, int v) {
    if (u == 0 || v == 0) {
        return Math.abs(u) + Math.abs(v);
    }

    // Ensure both numbers are positive
    u = Math.abs(u);
    v = Math.abs(v);

    // Remove common factors of 2
    int shift;
    for (shift = 0; ((u | v) & 1) == 0; ++shift) {
        u >>= 1;
        v >>= 1;
    }

    // Making sure u is odd
    while ((u & 1) == 0) {
        u >>= 1;
    }

    // Reduce v by removing factors of 2 and then subtracting u from v when v is greater
    do {
        while ((v & 1) == 0) {
            v >>= 1;
        }

        // Ensure u <= v and swap if necessary
        if (u > v) {
            int temp = u;
            u = v;
            v = temp;
        }

        v = v - u;
    } while (v != 0);

    return u << shift; // gcd is the non-zero remainder * 2^shift
}
