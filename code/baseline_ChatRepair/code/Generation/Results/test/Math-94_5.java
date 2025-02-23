public static int gcd(int u, int v) {
    if (u == 0 || v == 0) {
        return Math.abs(u) + Math.abs(v); 
    }
    
    if (u > 0) u = -u;
    if (v > 0) v = -v;

    
    int k = 0;
    while (((u & 1) == 0) && ((v & 1) == 0)) {
        u >>= 1; 
        v >>= 1; 
        k++;     
    }

    
    
    int t;
    
    if ((u & 1) == 1) {
        t = v;
    } else {
        t = -(u / 2);
    }

    do {
        
        while ((t & 1) == 0) {
            t /= 2;
        }

        
        if (t > 0) {
            u = -t;
        } else {
            v = t;
        }

        
        t = (v - u) / 2;
    } while (t != 0);

    
    return Math.abs(u) * (1 << k); 
}
