private Fraction(double value, double epsilon, int maxDenominator, int maxIterations)
throws FractionConversionException
{
    long overflow = Integer.MAX_VALUE;
    double r0 = value;
    long a0 = (long)FastMath.floor(r0);
    
    // Check if the input value is negative and close to -Integer.MAX_VALUE
    if (value < 0 && a0 > -overflow / 2) {
        throw new FractionConversionException(value, a0, 1l);
    }

    // ... (rest of the method remains the same)
}