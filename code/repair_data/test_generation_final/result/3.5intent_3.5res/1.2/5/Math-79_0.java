import org.apache.commons.math3.exception.MathRuntimeException;
import java.math.BigInteger;

public class MyClass {

    public static double distance(int[] p1, int[] p2) {
        BigInteger sum = BigInteger.ZERO;
        for (int i = 0; i < p1.length; i++) {
            final BigInteger dp = BigInteger.valueOf(p1[i]).subtract(BigInteger.valueOf(p2[i]));
            sum = sum.add(dp.multiply(dp));
        }
        return Math.sqrt(sum.doubleValue());
    }
    
    // Other methods and code...
}