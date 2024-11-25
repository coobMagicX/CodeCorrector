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

    long uk = Math.abs((long) u); // Calculate the absolute value of u as a long to avoid overflow
    long vk = Math.abs((long) v); // Calculate the absolute value of v as a long to avoid overflow

    while ((uk & 1) == 0 && (vk & 1) == 0 && uk <= Long.MAX_VALUE / 2 && vk <= Long.MAX_VALUE / 2) { // while u and v are
        uk /= 2;
        vk /= 2;
    }
    if (uk >= Long.MAX_VALUE / 2 || vk >= Long.MAX_VALUE / 2) {
        throw new ArithmeticException("overflow: gcd is too large");
    }

    long t = ((u & 1) == 1) ? v : -(uk / 2); // t negative: u was odd, v may be even (t replaces v)
                                                    // t positive: u was even, v is odd (t replaces u)

    do {
        while ((t & 1) == 0) { // while t is even..
            t /= 2; // cast out twos
        }
        if (t > 0) {
            uk = -t;
        } else {
            vk = t;
        }
        t = (vk - uk) / 2;
    } while (t != 0);
    return (int) (-uk * Math.abs((long) u >> 31)); // gcd is u*2^k
}