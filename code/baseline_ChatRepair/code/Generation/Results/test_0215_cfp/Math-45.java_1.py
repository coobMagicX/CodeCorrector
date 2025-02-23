import org.apache.commons.math3.exception.NotStrictlyPositiveException;

public OpenMapRealMatrix(int rowDimension, int columnDimension) {
    super(rowDimension, columnDimension);

    if (rowDimension <= 0 || columnDimension <= 0) {
        throw new Not