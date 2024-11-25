@Override
public boolean equals(Object obj) {
    if (this == obj)
        return true;
    if (!(obj instanceof Fraction))
        return false;
    Fraction other = (Fraction) obj;
    return getNumerator() * other.getDenominator() == 
           getDenominator() * other.getNumerator();
}

@Override
public int hashCode() {
    return 31 * getNumerator().hashCode() + getDenominator().hashCode();
}

public int compareTo(Fraction other) {
    if (this == null || other == null)
        throw new NullPointerException("Both fractions must be non-null");
    
    long thisValue = getNumerator() * other.getDenominator();
    long otherValue = other.getNumerator() * this.getDenominator();
    
    return Long.signum(thisValue - otherValue);
}