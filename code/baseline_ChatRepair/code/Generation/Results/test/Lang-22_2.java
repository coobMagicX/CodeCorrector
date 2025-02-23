private static int greatestCommonDivisor(int u, int v) {
    
    if (u == 0) return Math.abs(v);
    if (v == 0) return Math.abs(u);

    
    u = Math.abs(u);
    v = Math.abs(v);

    
    int shift;
    for (shift = 0; ((u | v) & 1) == 0; ++shift) {
        u >>= 1;
        v >>= 1;
    }
    
    
    while ((u & 1) == 0)
        u >>= 1;
    
    
    do {
        
        while ((v & 1) == 0)
            v >>= 1;

        
        if (u > v) {
            int t = v; v = u; u = t;
        }
        
        
        v = v - u;

    } while (v != 0);

    
    return u << shift;
}
