public static int gcd(int u, int v) {
    if (u == 0 || v == 0) {
        return Math.abs(u + v); // directly return the non-zero value or 0 if both are 0
    }
    // keep u and v negative, as negative integers range down to
    // -2^31, while positive numbers can only be as large as 2^31-1
    if (u > 0) u = -u; // make u negative
    if (v > 0) v = -v; // make v negative

    // B1. [Find power of 2]
    int k = 0;
    while ((u & 1) == 0 && (v & 1) == 0) { // while u and v are both even...
        u /= 2;
        v /= 2;
        k++; // cast out twos.
    }

    // B2. Initialize: u and v have been divided by 2^k and at least one is odd.
    int t = ((u & 1) == 1) ? v : -(u / 2); // start t with whichever is odd or switch u to halve

    do {
        // B4/B3: cast out twos from t.
        while ((t & 1) == 0) { // while t is even..
            t /= 2; // cast out twos
        }

        // B5 [reset max(u, v)]
        if (t > 0) {
            u = -t;
        } else {
            v = t;
        }

        // B6/B3. at this point both u and v should be odd.
        t = (v - u) / 2;
    } while (t != 0);

    return Math.abs(u) * (1 << k); // Correctly imposing the absolute value to avoid negative gcd
}
