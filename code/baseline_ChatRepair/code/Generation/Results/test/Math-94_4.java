public static int gcd(int u, int v) {
    
    
    if (u == 0 || v == 0) {
        return Math.abs(u + v); 
    }
    
    
    
    if (u > 0) u = -u; 
    if (v > 0) v = -v; 

    
    
    int k = 0;
    while ((u & 1) == 0 && (v & 1) == 0) { 
        u >>= 1; 
        v >>= 1; 
        k++; 
    }

    
    while ((u & 1) == 0) u >>= 1; 
    while (v != 0) {
        while ((v & 1) == 0) v >>= 1; 
        if (u > v) {
            int temp = v;
            v = u;
            u = temp;
        }
        v = v - u; 
    }

    
    return Math.abs(u * (1 << k));
}
