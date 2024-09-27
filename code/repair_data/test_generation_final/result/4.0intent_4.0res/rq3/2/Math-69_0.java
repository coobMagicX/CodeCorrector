import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.exception.MathException;

public class CorrelationTest {
    private RealMatrix correlationMatrix;
    private int nObs;

    public CorrelationTest(RealMatrix correlationMatrix, int nObs) {
        this.correlationMatrix = correlationMatrix;
        this.nObs = nObs;
    }

    public RealMatrix getCorrelationPValues() throws MathException {
        TDistribution tDistribution = new TDistribution(nObs - 2);
        int nVars = correlationMatrix.getColumnDimension();
        double[][] out = new double[nVars][nVars];
        for (int i = 0; i < nVars; i++) {
            for (int j = 0; j < nVars; j++) {
                if (i == j) {
                    out[i][j] = 0.0; // Ensuring that the diagonal is always 0 (no p-value for correlation with itself)
                } else {
                    double r = correlationMatrix.getEntry(i, j);
                    double t = Math.abs(r * Math.sqrt((nObs - 2) / (1 - r * r)));
                    out[i][j] = 2 * (1 - tDistribution.cumulativeProbability(t));
                }
            }
        }
        return new BlockRealMatrix(out);
    }
}