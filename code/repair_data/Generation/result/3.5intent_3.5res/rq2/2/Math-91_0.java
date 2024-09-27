public int compareTo(Fraction object) {
    Fraction reducedThis = getReducedFraction(numerator, denominator);
    Fraction reducedObject = getReducedFraction(object.numerator, object.denominator);
    return reducedThis.compareTo(reducedObject);
}