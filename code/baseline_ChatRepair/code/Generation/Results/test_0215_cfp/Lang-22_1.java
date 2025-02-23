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
    
    
    

    while ((u & 1) == 0) 
        u >>= 1; 
    
    
    do {
        while ((v & 1) == 0) 
            v >>= 1;
        
        
        if (u > v) {
            u -= v;
        } else {
            v -= u;
        }

        
        u >>= 1; 
    } while (u != 0);
    
    return v << k; 
}
