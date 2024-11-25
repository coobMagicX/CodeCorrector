import static java.lang.Math.atan2;
import static java.lang.Math.PI;

public void atan2(final double[] y, final int yOffset,
                  final double[] x, final int xOffset,
                  final double[] result, final int resultOffset) {

    // compute r = sqrt(x^2 + y^2)
    double rSquared = 0.0;
    for (int i = 0; i < getSize(); ++i) {
        rSquared += x[xOffset + i] * x[xOffset + i] + y[yOffset + i] * y[yOffset + i];
    }
    final double r = Math.sqrt(rSquared);

    // fix value to take special cases (+0/+0, +0/-0, -0/+0, -0/-0, +/-infinity) correctly
    double atan2Result;
    if (Double.isNaN(r) || Double.isInfinite(r)) {
        // Handle infinity or NaN for r by returning PI/4 if both x and y are 0,
        // or returning an appropriate value for infinity.
        if (Double.compare(x[xOffset], 0.0) == 0 && Double.compare(y[yOffset], 0.0) == 0) {
            atan2Result = Double.NaN; // atan2(0, 0) is undefined
        } else if (Double.isInfinite(x[xOffset]) || Double.isInfinite(y[yOffset])) {
            atan2Result = Double.isPositive(x[xOffset]) ? PI / 4 : -PI / 4;
        } else {
            atan2Result = Double.NaN; // Undefined result for other cases
        }
    } else {
        // Normal case computation
        if (Double.compare(x[xOffset], 0.0) >= 0) {
            // compute atan2(y, x) = 2 atan(y / (r + x))
            double yOverRPlusX = y[yOffset] / (r + x[xOffset]);
            atan2Result = 2 * Math.atan(yOverRPlusX);
        } else {
            // compute atan2(y, x) = +/- pi - 2 atan(y / (r - x))
            double yOverRMinusX = y[yOffset] / (r - x[xOffset]);
            atan2Result = Double.compare(yOverRMinusX, 0.0) <= 0 ? PI : -PI;
            atan2Result -= 2 * Math.atan(yOverRMinusX);
        }
    }

    // Store the result
    result[resultOffset] = atan2Result;

    // Copy over the rest of the results if necessary (assuming single value result array)
    for (int i = 1; i < getSize(); ++i) {
        result[resultOffset + i] = result[resultOffset];
    }
}