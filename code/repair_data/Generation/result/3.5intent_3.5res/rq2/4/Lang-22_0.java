private static int greatestCommonDivisor(int u, int v) {
    if (Math.abs(u) <= 1 || Math.abs(v) <= 1) {
        return 1;
    }
    
    if (u == Integer.MIN_VALUE) u++;
    if (v == Integer.MIN_VALUE) v++;
    
    if (u>0) { u=-u; }
    if (v>0) { v=-v; }
    
    int k=0;
    while ((u&1)==0 && (v&1)==0 && k<31) {
        u/=2; v/=2; k++;
    }
    if (k==31) {
        throw new ArithmeticException("overflow: gcd is 2^31");
    }
    
    int t = ((u&1)==1) ? v : -(u/2);
    
    do {
        while ((t&1)==0) {
            t/=2;
        }
        
        if (t>0) {
            u = -t;
        } else {
            v = t;
        }
        
        t = (v - u)/2;
    } while (t!=0);
    
    return -u*(1<<k);
}