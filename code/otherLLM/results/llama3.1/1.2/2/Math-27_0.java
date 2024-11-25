public double percentageValue() {
    int numer = getNumerator();
    int deno = getDenominator();
    if (numer == 0 && deno == 0) return 0;
    
    long longNumer = (long)numer; // Safe casting
    long longDeno = (long)deno;     // Safe casting
    
    long result = (longNumer * 100) / longDeno;  // Overflow handling and proper rounding
    
    return Math.floor(result);  // Proper rounding
}