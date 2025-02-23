private double calculateAsymptoticPValue(final double Umin, final int n1, final int n2)
    throws ConvergenceException, MaxCountExceededException {

    
    final long n1n2Product = (long) n1 * (long) n2;

    
    
    final double EU = n1n2Product / 2.0;
    final double VarU = n1n2Product * (n1 + n2 + 1) / 12.0;

    
    final double z = (Umin - EU) / Math.sqrt(VarU); 

    
    final NormalDistribution standardNormal = new NormalDistribution(0, 1);

    
    double pValue = standardNormal.cumulativeProbability(z);
    return 2 * (z < 0 ? pValue :