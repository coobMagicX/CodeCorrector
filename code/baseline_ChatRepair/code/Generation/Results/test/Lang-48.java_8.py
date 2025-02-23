import java.math.BigDecimal;

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
            setEquals(false);
            return this;
        }

        if (!lhs.getClass().isArray()) {
            // Special handling for objects that override equals, for example BigDecimal
            // Checks whether types are directly comparable and not array types.
            if (lhs.getClass().equals(rhs.getClass()) && lhs instanceof BigDecimal) {
                // Use compareTo for BigDecimal comparison
                isEquals = ((BigDecimal) lhs).compareTo((BigDecimal) rhs) == 0;
            } else {
                isEquals = lhs.equals(rhs);
            }
        } else {
            // Handle array types
            handleArrayComparison(lhs, rhs);
        }
        return this;
    }

    private void handleArrayComparison(Object lhs, Object rhs) {
        // Handle the comparison logic for array types
        Class componentType = lhs.getClass().getComponentType();
        if (componentType.equals(rhs.getClass().getComponentType())) {
            if (componentType.isPrimitive()) {
                if (lhs instanceof long[]) {
                    append((long[]) lhs, (long[]) rhs);
                } else if (lhs instanceof int[]) {
                    append((int[]) lhs, (int[]) rhs);
                } else if (lhs instanceof short[]) {
                    append((short[]) lhs, (short[]) rhs);
                } else if (lhs instanceof char[]) {
                    append((char[]) lhs, (char[]) rhs);
                } else if (lhs instanceof byte[]) {
                    append((byte[]) lhs, (byte[]) rhs);
                } else if (lhs instanceof double[]) {
                    append((double[]) lhs, (double[]) rhs);
                } else if (lhs instanceof float[]) {
                    append((float[]) lhs, (float[]) rhs);
                } else if (lhs instanceof boolean[]) {
                    append((boolean[]) lhs, (boolean[]) rhs);
                } else {
                    // Not a recognized primitive array
                    isEquals = false;
                }
            } else {
                // Array of objects
                append((Object[]) lhs, (Object[]) rhs);
            }
        } else {
            // Not the same array type
            setEquals(false);
        }
    }

    public void setEquals(boolean value) {
        isEquals = value;
    }
}
