public static int gcd(int u, int v) {
    if (u == 0) return Math.abs(v);
    if (v == 0) return Math.abs(u);

    // Make u and v non-negative.
    u = Math.abs(u);
    v = Math.abs(v);

    // Count the number of twos in common factors of u and v
    int twos = Integer.numberOfTrailingZeros(u | v);
    
    // Divide u and v by 2 until odd
    u >>= Integer.numberOfTrailingZeros(u);
    v >>= Integer.numberOfTrailingZeros(v);
    
    while (u != v) {
        if (u > v) {
            u -= v;
            u >>= Integer.numberOfTrailingZeros(u);
        } else {
            v -= u;
            v >>= Integer.numberOfTrailingZeros(v);
        }
    }

    // Restore common factors of 2
    return u << twos;
}
