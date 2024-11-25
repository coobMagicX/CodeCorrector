private static int greatestCommonDivisor(int u, int v) {
    if (Math.abs(u) <= 1 || Math.abs(v) <= 1) {
        return 1;
    }

    // Check for overflow in GCD calculation
    if (Math.abs(u) >= Integer.MAX_VALUE / 2 || Math.abs(v) >= Integer.MAX_VALUE / 2) {
        throw new ArithmeticException("overflow: gcd is too large");
    }

    // Ensure consistent sign handling
    int minSign = Math.min((u < 0 ? -1 : 1), (v < 0 ? -1 : 1));
    u *= minSign;
    v *= minSign;

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
    //     one is odd.
    int t = ((u & 1) == 1) ? v : -(u / 2); /*B3*/
    do {
        /* assert u < 0 && v < 0; */
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
    return -u * (1 << k); 
}