public int compareTo(Fraction object) {
    
    long adjustedNumThis = this.numerator * (long) object.denominator;
    long adjustedNumObject