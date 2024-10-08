    public void testGcd() {
        int a = 30;
        int b = 50;
        int c = 77;

        assertEquals(0, MathUtils.gcd(0, 0));

        assertEquals(b, MathUtils.gcd(0, b));
        assertEquals(a, MathUtils.gcd(a, 0));
        assertEquals(b, MathUtils.gcd(0, -b));
        assertEquals(a, MathUtils.gcd(-a, 0));

        assertEquals(10, MathUtils.gcd(a, b));
        assertEquals(10, MathUtils.gcd(-a, b));
        assertEquals(10, MathUtils.gcd(a, -b));
        assertEquals(10, MathUtils.gcd(-a, -b));

        assertEquals(1, MathUtils.gcd(a, c));
        assertEquals(1, MathUtils.gcd(-a, c));
        assertEquals(1, MathUtils.gcd(a, -c));
        assertEquals(1, MathUtils.gcd(-a, -c));

        assertEquals(3 * (1<<15), MathUtils.gcd(3 * (1<<20), 9 * (1<<15)));

    }