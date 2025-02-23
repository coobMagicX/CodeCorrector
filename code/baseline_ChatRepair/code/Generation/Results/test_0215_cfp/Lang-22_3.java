private static int greatestCommonDivisor(int u, int v) {
    if (u == 0 || v == 0) return Math.abs(u + v); 

    
    u = Math.abs(u);
    v = Math.abs(v);

    
    int shift;
    for (shift = 0; (u | v) % 2 == 0; ++shift) {
        u >>= 1;
        v >>= 1;
    }

    
    while (u % 2 == 0) {
        u >>= 1;
    }

    
    do {
        
        while (v % 2 == 0) {
            v >>= 1;
        }

        
        if (u > v) {
            int temp = u;
            u = v;
            v = temp;
        }
        
        
        v = v - u;
    } while (v != 0);

    
    return u << shift; 
}
