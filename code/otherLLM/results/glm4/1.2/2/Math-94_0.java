public static int gcd(int u, int v) {
    if (u == 0) return Math.abs(v);
    if (v == 0) return Math.abs(u);

    // Keep the signs of the inputs to avoid overflow with negative numbers.
    long absoluteU = Math.abs((long)u);
    long absoluteV = Math.abs((long)v);

    // B1. [Find power of 2]
    int k = 0;
    while ((absoluteU & 1) == 0 && (absoluteV & 1) == 0 && k < 31) { // while u and v are both even...
        absoluteU /= 2;
        absoluteV /= 2;
        k++; // cast out twos.
    }
    if (k == 31) {
        throw new ArithmeticException("overflow: gcd is 2^31");
    }

    // B2. Initialize: u and v have been divided by 2^k and at least one is odd.
    int t = ((absoluteU & 1) == 1) ? (int)absoluteV : -(int)(absoluteU / 2);
    // t negative: u was odd, v may be even (t replaces v)
    // t positive: u was even, v is odd (t replaces u)

    do {
        // B4/B3: cast out twos from t.
        while ((t & 1) == 0) { // while t is even..
            t /= 2; // cast out twos
        }
        // B5 [reset max(u,v)]
        if (t > 0) {
            u = -(int)t;
        } else {
            v = -(int)t;
        }
        // B6/B3. at this point both u and v should be odd.
        t = (v - u) / 2;
    } while (t != 0);

    return -u * (1 << k); // gcd is u*2^k
}