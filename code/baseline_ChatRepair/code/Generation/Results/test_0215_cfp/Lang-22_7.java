private static int greatestCommonDivisor(int u, int v) {
    
    if (u == 0) return Math.abs(v);
    if (v == 0) return Math.abs(u);

    
    u = Math.abs(u);
    v = Math.abs(v);

    
    int k = 0;
    while ((u & 1) == 0 && (v & 1) == 0) {
        u >>= 1; 
        v >>= 1; 
        k++; 
    }
    
    
    int t = (u & 1) == 1 ? v : -((u >> 1));
    
    do {
        
        while ((t & 1) == 0) {
            t >>= 1; 
        }
        
        
        if (t > 0) {
            u = t; 
        } else {
            v = -t; 
        }
        
        
        t = (u - v) >> 1;
        
    } while (t != 0);

    
    return Math.abs(u) << k;
}
