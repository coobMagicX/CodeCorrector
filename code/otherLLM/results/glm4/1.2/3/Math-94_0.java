public static int gcd(int u, int v) {
    if (u == 0 || v == 0) {
        return Math.abs(u) + Math.abs(v);
    }
    // keep u and v negative, as negative integers range down to -2^31
    if (u > 0) {
        u = -u;
    } // make u negative
    if (v > 0) {
        v = -v;
    } // make v negative

    int k = 0;
    while ((u & 1) == 0 && (v & 1) == 0 && k < 31) { // while u and v are both even...
        u /= 2;
        v /= 2;
        k++; // cast out twos.
    }
    if (k == 31) {
        throw new ArithmeticException("overflow: gcd is 2^31");
    }

    int t = ((u & 1) == 1) ? v : -(u / 2);
    do {
        while ((t & 1) == 0) { // while t is even..
            t /= 2; // cast out twos
            if (t == 0) break; // If after casting out twos, t becomes zero, exit the loop.
        }
        if (t > 0) {
            u = -t;
        } else {
            v = t;
        }
        t = (v - u) / 2;
    } while (t != 0);

    return -u * (1 << k); // gcd is u*2^k
}