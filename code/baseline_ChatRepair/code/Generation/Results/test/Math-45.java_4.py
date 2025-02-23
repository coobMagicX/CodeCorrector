public OpenMapRealMatrix(int rowDimension, int columnDimension) {
    super(rowDimension, columnDimension);

    if (rowDimension <= 0 || columnDimension <= 0) {
        throw new org.apache.commons.math.exception.NotStrictlyPositiveException(
            "Row and column dimensions must be positive."
        );
    }

    if (((long) rowDimension) * ((long) columnDimension) > Integer.MAX_VALUE) {
        throw new org.apache.commons.math.exception.NumberIsTooLargeException(
            ((long) rowDimension) * ((long) columnDimension), Integer.MAX_VALUE, false
        );
    }

    this.rows = rowDimension;
    this.columns = columnDimension;
    this