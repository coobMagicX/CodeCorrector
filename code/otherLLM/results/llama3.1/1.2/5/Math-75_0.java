public double getPct(Object v) {
    if (!(v instanceof Comparable)) {
        throw new IllegalArgumentException("Input object must be comparable");
    }
    
    double cumulativePct = getCumPct(v);
    if (cumulativePct == -1.0) { // assuming getCumPct() returns -1.0 for non-numeric inputs
        return 0.0;
    }
    
    return cumulativePct;
}