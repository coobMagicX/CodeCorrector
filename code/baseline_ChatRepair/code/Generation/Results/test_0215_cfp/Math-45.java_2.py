import org.apache.commons.math3.exception.NotStrictlyPositiveException;

public OpenMapRealMatrix(int rowDimension, int columnDimension) {
    super(rowDimension, columnDimension);

    if (rowDimension <= 0 || columnDimension <= 0) {
        throw new NotStrictlyPositiveException(rowDimension <= 0 ? rowDimension : columnDimension);
    }

    this.rows = rowDimension;
    this.columns = columnDimension;
    this.entries = new OpenIntToDoubleHashMap(0.0);
}
