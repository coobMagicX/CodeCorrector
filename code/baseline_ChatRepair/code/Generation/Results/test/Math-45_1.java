public OpenMapRealMatrix(int rowDimension, int columnDimension) throws NumberIsTooLargeException {
    super(rowDimension, columnDimension);
    
    
    if (rowDimension <= 0 || columnDimension <= 0) {
        throw new IllegalArgumentException