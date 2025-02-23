private static int greatestCommonDivisor(int u, int v) {
    if (u == 0) return v;
    if (v == 0) return u;
    
    // Excluding negative zero case
    u = Math.abs(u);
    v = Math.abs(v);
    
    // B1. [Find power of 2]
    int k;
    for (k = 0; ((u | v) & 1) == 0; ++k) {
        u >>= 1;
        v >>= 1;
    }
    
    // Dividing the even number
    while ((u & 1) == 0) u >>= 1;
    while ((v & 1) == 0) v >>= 1;
    
    // From here, both u and v are odd
    while (u != v) {
        if (u > v) {
            u = (u - v) >> 1;
        } else {
            v = (v - u) >> 1;
        }
        
        // Remove all factors of 2 in u and v
        while ((u & 1) == 0) u >>= 1;
        while ((v & 1) == 0) v >>= 1;
    }
    
    return u << k;
}
