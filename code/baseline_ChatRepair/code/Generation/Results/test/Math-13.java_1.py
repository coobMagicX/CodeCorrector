private RealMatrix squareRoot(RealMatrix m) {
    if (m == null) {
        throw new IllegalArgumentException("Matrix cannot be null");
    }
    
    // Check the size of the matrix
    int size = m.getColumnDimension();
    if (size > 1000) {  // Adjust this threshold based on memory constraints and typical matrix sizes you expect
        throw new IllegalArgumentException("Matrix size too large for operation, may cause memory issues");
    }

