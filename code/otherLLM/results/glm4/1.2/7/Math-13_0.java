private RealMatrix squareRoot(RealMatrix m) {
    EigenDecomposition dec = new EigenDecomposition(m);
    // Store the reference in a local variable if you need it later on
    RealMatrix sqrtOfM = dec.getSquareRoot();
    
    // Ensure that the 'dec' object is eligible for garbage collection after we are done using it.
    dec = null;
    
    return sqrtOfM;
}