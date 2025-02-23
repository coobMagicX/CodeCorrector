private static int greatestCommonDivisor(int u, int v) {
    if (u == 0) return Math.abs(v);
    if (v == 0) return Math.abs(u);

    
    u = Math.abs(u);
    v = Math.abs(v);

    
    int k = 0;
    while ((u % 2 == 0) && (v % 2 == 0)) {
        u /= 2;
        v /= 2;
        k++;
    }

    
    if (k == 31) {
        throw new ArithmeticException("Overflow: gcd cannot be represented.");
    }

    
    while (u != v) {
        if (u % 2 == 0) {
            u /= 2;
        } else if (v % 2 == 0) {
            v /= 2;
        } else if (u > v) {
            u = (u - v) / 2;
        } else {
            v = (v - u) / 2;
        }
    }

    
    return u * (1 << k);
}
