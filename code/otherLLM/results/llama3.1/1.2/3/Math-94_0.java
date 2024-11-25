public static int gcd(int u, int v) {
    if (u * v == 0) {
        return (Math.abs(u) + Math.abs(v));
    }
    // keep u and v negative, as negative integers range down to
    // -2^31, while positive numbers can only be as large as 2^31-1
    // (i.e. we can't necessarily negate a negative number without
    // overflow)
    /* assert u!=0 && v!=0; */
    if (u > 0) {
        u = -u;
    } // make u negative
    if (v > 0) {
        v = -v;
    } // make v negative

    long tmpU = u, tmpV = v; // Use long to handle overflow
    int k = 0;
    while ((tmpU & 1) == 0 && (tmpV & 1) == 0 && k < 31) { // while u and v are
                                                                // both even...
        tmpU /= 2;
        tmpV /= 2;
        k++; // cast out twos.
    }
    if (k == 31) {
        throw new ArithmeticException("overflow: gcd is 2^31");
    }

    long t = ((tmpU & 1) == 1) ? tmpV : -(tmpU / 2);
    do {
        /* assert u<0 && v<0; */
        // B4/B3: cast out twos from t.
        while ((t & 1) == 0) { // while t is even..
            t /= 2; // cast out twos
        }
        // B5 [reset max(u,v)]
        if (t > 0) {
            tmpU = -t;
        } else {
            tmpV = t;
        }
        // B6/B3. at this point both u and v should be odd.
        t = (tmpV - tmpU) / 2;
        // |u| larger: t positive (replace u)
        // |v| larger: t negative (replace v)
    } while (t != 0);

    int result = -((int)tmpU * (1 << k));
    if (result > Integer.MAX_VALUE || result < Integer.MIN_VALUE) {
        throw new ArithmeticException("overflow: gcd is out of range");
    }
    return result;
}