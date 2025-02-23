import org.apache.commons.math.exception.NumberIsTooLargeException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.linear.AbstractRealMatrix;
import org.apache.commons.math.util.OpenIntToDoubleHashMap;

public class OpenMapRealMatrix extends AbstractRealMatrix {
    private final int rows;
    private final int columns;
    private final OpenIntToDoubleHashMap entries;

    public OpenMapRealMatrix(int rowDimension, int columnDimension) {
        super(rowDimension, columnDimension);

        if (rowDimension <= 0 || columnDimension <= 0) {
            throw new NotStrictlyPositiveException(
                "Row and column dimensions must be positive. Provided rowDimension: " + rowDimension