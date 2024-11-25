public double[] repairAndDecode(final double[] x) {
    // Encode the input array within the specified bounds
    final double[] encodedX = encode(x);
    
    // Perform optimization on the encoded values
    final double[] optimizedEncodedX = optimizer.optimize(encodedX, fitnessFunction);
    
    // Decode the optimized and encoded values back to the original domain
    return decode(optimizedEncodedX);
}