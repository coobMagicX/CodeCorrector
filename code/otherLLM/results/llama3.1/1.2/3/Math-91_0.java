public int compareTo(Fraction object) {
    double nOd = doubleValue();
    double dOn = object.doubleValue();
    
    // Review comparison logic and consider overflow handling, validate equality condition, 
    // and rethink approximation and precision.
    return (nOd < dOn) ? -1 : ((nOd > dOn) ? +1 : ((equals(object)) ? 0 : ((MathUtils.gcd(numerator, denominator) == 1 && MathUtils.gcd(object.numerator, object.denominator) == 1) ? (numerator * object.denominator - denominator * object.numerator) < 0 ? -1 : ((numerator * object.denominator - denominator * object.numerator) > 0 ? +1 : 0)) : (Math.abs(nOd - dOn) < 1e-9) ? 0 : ((nOd < dOn) ? -1 : ((nOd > dOn) ? +1 : 0))));
}