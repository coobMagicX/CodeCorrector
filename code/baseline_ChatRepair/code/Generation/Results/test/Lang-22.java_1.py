private static int greatestCommonDivisor(int u, int v) {
    if (u == 0) return Math.abs(v);
    if (v == 0) return Math.abs(u);

    // Make both numbers positive
    u = Math.abs(u);
    v = Math.abs(v);

    // Use the Euclidean algorithm to find the gcd
    while (v != 0) {
        int t = v;
        v = u % v;
        u = t;
    }
    
    return u; // Return the absolute value since gcd is always positive
}
