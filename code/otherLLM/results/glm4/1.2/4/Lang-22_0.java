private static int greatestCommonDivisor(int u, int v) {
    // From Commons Math:
    if (Math.abs(u) <= 1 || Math.abs(v) <= 1) {
        return 1;
    }
    
    // Keep u and v negative to avoid overflow when negating
    if (u > 0) { u = -u; }
    if (v > 0) { v = -v; }

    // B1. [Find power of 2]
    int k = 0;
    while ((u & 1) == 0 && (v & 1) == 0 && k < 31) {
        u /= 2;
        v /= 2;
        k++;
    }
    if (k == 31) {
        throw new ArithmeticException("overflow: gcd is 2^31");
    }

    // B2. Initialize: u and v have been divided by 2^k and at least
    // one is odd.
    int t = ((u & 1) == 1) ? v : -(u / 2);
    
    do {
        // B4/B3: cast out twos from t.
        while ((t & 1) == 0) {
            t /= 2;
        }
        
        // B5 [reset max(u,v)]
        if (t > 0) {
            u = -t;
        } else {
            v = t;
        }
        
        // B6/B3. at this point both u and v should be odd.
        t = (v - u) / 2;
    } while (t != 0);

    // Use long for multiplication to avoid overflow
    long result = (-u) * (1L << k);
    
    // Check for overflow and return the correct result
    if (result > Integer.MAX_VALUE || result < Integer.MIN_VALUE) {
        throw new ArithmeticException("overflow: gcd exceeds int range");
    }
    
    return (int) result;
}