private static int greatestCommonDivisor(int u, int v) {
    if (Math.abs(u) <= 1 || Math.abs(v) <= 1) {
        return 1;
    }
    if (u>0) { u=-u; } // make u negative
    if (v>0) { v=-v; } // make v negative

    int k=0;
    while ((u&1)==0 && (v&1)==0 && k<31) {
        u/=2;
        v/=2;
        k++;
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
    
    // Fix integer overflow issue
    long gcd = Math.abs((long)u * v);
    return (int)(gcd / greatestCommonDivisor(Math.abs(u), Math.abs(v)));
}

private static int getReducedFraction(int numerator, int denominator) {
    int gcdValue = greatestCommonDivisor(numerator, denominator);
    
    // Fix negative value handling
    if (numerator < 0 && denominator > 0 || numerator > 0 && denominator < 0) {
        return -getReducedFraction(-numerator, -denominator);
    }
    
    return Math.abs(numerator / gcdValue) * Math.abs(denominator / gcdValue);
}