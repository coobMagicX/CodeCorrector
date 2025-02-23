import java.math.BigDecimal;
import java.util.Arrays;

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

        Class<?> lhsClass = lhs.getClass();
        Class<?> rhsClass = rhs.getClass();

        if (lhsClass.isArray() && rhsClass.isArray()) {
            handleArrayComparison(lhs, rhs);
        } else if (lhs instanceof BigDecimal && rhs instanceof BigDecimal) {
            // Handle BigDecimal comparisons
            isEquals = ((BigDecimal) lhs).compareTo((BigDecimal) rhs) == 0;
        } else {
            // Handle non-array object comparisons
            isEquals = lhs.equals(rhs);
        }

        return this;
    }

    private void handleArrayComparison(Object lhs, Object rhs) {
        if (lhs.getClass() != rhs.getClass() || Array.getLength(lhs) != Array.getLength(rhs)) {
            isEquals = false;
            return;
        }

        int length = Array.getLength(lhs);
        for (int i = 0; i < length; i++) {
            Object lhsElement = Array.get(lhs, i);
            Object rhsElement = Array.get(rhs, i);
            new EqualsBuilder().append(lhsElement, rhsElement);
            if (!isEquals) {
                return;
            }
        }
    }

    public boolean isEquals() {
        return isEquals;
    }
}

// Example usage:
class Main {
    public static void main(String[] args) {
        BigDecimal db1 = new BigDecimal("2.00");
        BigDecimal db2 = new BigDecimal("2.0");
        EqualsBuilder builder = new EqualsBuilder();
        boolean result = builder.append(db1, db2).isEquals();
        System.out.println("BigDecimal comparison result: " + result); // Expected to print true
    }
}
