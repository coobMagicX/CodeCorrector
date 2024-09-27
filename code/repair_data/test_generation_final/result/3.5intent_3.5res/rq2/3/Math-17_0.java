// Import the necessary dependencies for the test
import org.apache.commons.math3.dfp.Dfp;
import org.apache.commons.math3.dfp.DfpMath;

public class MyClass {
    // Define the multiply method
    public Dfp multiply(final int x) {
        return multiplyFast(x);
    }
    
    // Define the multiplyFast method
    private Dfp multiplyFast(final int x) {
        // Perform the multiplication using the existing methods and data
        Dfp dfpX = DfpMath.toDfp(x);
        Dfp dfpMultiplier = DfpMath.toDfp(10000);
        return dfpX.multiply(dfpMultiplier);
    }
    
    // Define the test method
    public void test() {
        // Create an instance of MyClass
        MyClass myClass = new MyClass();
        
        // Define the expected result
        Dfp expectedResult = DfpMath.toDfp(10000);
        
        // Call the multiply method
        Dfp result = myClass.multiply(1);
        
        // Compare the expected and actual results
        if (result.equals(expectedResult)) {
            System.out.println("Test Passed");
        } else {
            System.out.println("Test Failed");
        }
    }
}