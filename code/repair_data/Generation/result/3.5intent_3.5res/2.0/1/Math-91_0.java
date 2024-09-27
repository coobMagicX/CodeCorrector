public int compareTo(Fraction object) {
    int num1 = numerator * object.denominator;
    int num2 = object.numerator * denominator;
    
    return Integer.compare(num1, num2);
}
