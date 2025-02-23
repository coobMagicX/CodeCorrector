import java.math.BigDecimal;
import java.lang.reflect.Array;

public class EqualsBuilder {
    private boolean isEquals = true;

    public EqualsBuilder append(Object lhs, Object rhs) {
        if (!isEquals) {
            return this;
        }

        if (lhs == rhs) {
            return this;
        }

        if (lhs == null || rhs == null) {
            isEquals = false;
            return this;
        }

        // Specific handling for BigDecimal due to scale considerations
        if (lhs instanceof BigDecimal && rhs instanceof BigDecimal) {
            isEquals = ((BigDecimal) lhs).compareTo((BigDecimal) rhs) == 0;
            return this;
        }
        
        // Check if the objects are arrays
        if (lhs.getClass().isArray() && rhs.getClass().isArray()) {
            handleArrayComparison(lhs, rhs);
        } else {
            // Default case for non-array objects
            isEquals = lhs.equals(rhs);
        }

        return this;
    }

    private void handleArrayComparison(Object lhs, Object rhs) {
        if (Array.getLength(lhs) != Array.getLength(rhs)) {
            isEquals = false;
            return;
        }

        for (int i = 0; i < Array.getLength(lhs); i++) {
            Object lhsElement = Array.get(lhs, i);
            Object rhsElement = Array.get(rhs, i);

            // Recursive append for elements of the array
            append(lhsElement, rhsElement);
            if (!isEquals) {
                return;
            }
        }
    }

    // ... other append methods for primitive arrays might also be defined here

    public boolean isEquals() {
        return isEquals;
    }
}

// Example Usage
public class Example {
    public void testBigDecimal() {
        BigDecimal lhs = new BigDecimal("2.0");
        BigDecimal rhs = new BigDecimal("2.00");
        boolean result = new EqualsBuilder().append(lhs, rhs).isEquals();
        System.out.println("Result: " + result); // should print true
    }
}
