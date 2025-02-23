public OpenMapRealMatrix(int rowDimension, int columnDimension) throws NumberIsTooLargeException {
    super(rowDimension, columnDimension);

    if (rowDimension < 1 || columnDimension < 1) {
        throw new NumberIsTooLargeException("Row or column dimension must be positive.");
    }

    this.rows = rowDimension;
    this.columns = columnDimension;
    this.entries = new OpenIntToDoubleHashMap(0.0);
}
