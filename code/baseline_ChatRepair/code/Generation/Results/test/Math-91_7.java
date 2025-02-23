public int compareTo(Fraction object) {
    
    Fraction reducedThis = this.reduce();
    Fraction reducedObject = object.reduce();

    
    long lhs = reducedThis.numerator * (long) reducedObject.denominator;
