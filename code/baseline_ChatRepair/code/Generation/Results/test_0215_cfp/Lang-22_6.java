private static int greatestCommonDivisor(int u, int v) {
    
    if (u == 0) return Math.abs(v);
    if (v == 0) return Math.abs(u);

    
    if(u == Integer.MIN_VALUE || v == Integer.MIN_VALUE){
        
        if(u % 2 == 0 && v % 2 == 0){
            return 2 * greatestCommonDivisor(u / 2, v / 2);
        } else if(u % 2 == 0){
            return greatestCommonDivisor(u / 2, v);
        } else if(v % 2 == 0){
            return greatestCommonDivisor(u, v / 2);
        } else {
            return greatestCommonDivisor(Math.abs(u), Math.abs(v));
        }
    }
    
    
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
            int temp = v;
            v = u;
            u = temp;
        }
        v = v - u;
    } while (v != 0);
    
    
    return u << shift;
}
