private Fraction(double value, double epsilon, int maxDenominator, int maxIterations)
    throws FractionConversionException {
    
    double overflow = Integer.MAX_VALUE;  
    double underflow = Integer.MIN_VALUE; 
    double r0 = value;
    double a0 = FastMath.floor(r0);

    
    if (a0 > overflow || a0 < underflow) {
        throw new FractionConversionException(value, (long)a0, 1L);
    }

    
    if (FastMath.abs(a0 - value) < epsilon) {
        this.numerator = (int) a0;
        this.denominator = 1;
        return;
    }

    double p0 = 1;
    double q0 = 0;
    double p1 = a0;
    double q1 = 1;

    double p2;
    double q2;

    int n = 0;
    boolean stop = false;
    do {
        ++n;
        double r1 = 1.0 / (r0 - a0);
        double a1 = FastMath.floor(r1);
        p2 = (a1 * p1) + p0;
        q2 = (a1 * q1) + q0;

        
        if (p2 > overflow || p2 < underflow || q2 > overflow || q2 < underflow) {
            throw new FractionConversionException(value, (long)p2, (long)q2);
        }

        double convergent = p2 / q2;
        if (n < maxIterations && FastMath.abs(convergent - value) > epsilon && q2 < maxDenominator) {
            p0 = p1;
            p1 = p2;
            q0 = q1;
            q1 = q2;
            a0 = a1;
            r0 = r1;
        } else {
            stop = true;
        }
    } while (!stop);

    if (n >= maxIterations) {
        throw new FractionConversionException(value, maxIterations);
    }

    if (q2 < maxDenominator) {
        this.numerator = (int) p2;
        this.denominator = (int) q2;
    } else {
        this.numerator = (int) p1;
        this.denominator = (int) q1;
    }
}
