public OpenMapRealMatrix(int rowDimension, int columnDimension) throws NumberIsTooLargeException {
    super(rowDimension, columnDimension);
    
    // Set maximum dimensions limit as per your context
    long maxDimension = Integer.MAX_VALUE; 
    
    if ((long) rowDimension * columnDimension > maxDimension) {
        throw new NumberIsTooLargeException((long) rowDimension * columnDimension, maxDimension, false);
    }

    this.rows = rowDimension;
    this.columns = columnDimension;
    this.entries = new OpenIntToDoubleHashMap(0.0);
}
