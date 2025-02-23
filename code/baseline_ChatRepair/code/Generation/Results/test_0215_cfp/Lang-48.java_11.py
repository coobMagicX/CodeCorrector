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

        Class lhsClass = lhs.getClass();
        Class rhsClass = rhs.getClass();

        if (lhs.getClass().isArray() && rhs.getClass().isArray()) {
            if (!lhsClass.equals(rhsClass)) {
                setEquals(false);
            } else {
                appendArrays(lhs, rhs);
            }
        } else {
            isEquals = lhs.equals(rhs);
        }
        return this;
    }

    private void appendArrays(Object lhs, Object rhs) {
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
            // Not an array of primitives
            append((Object[]) lhs, (Object[]) rhs);
        }
    }

    private void append(long[] lhs, long[] rhs) {
        if (lhs.length != rhs.length) {
            setEquals(false);
            return;
        }
        for (int i = 0; i < lhs.length; i++) {
            if (lhs[i] != rhs[i]) {
                setEquals(false);
                return;
            }
        }
    }
    
    // Similar append methods for other array types...

    public boolean isEquals() {
        return isEquals;
    }

    private void setEquals(boolean isEquals) {
        this.isEquals = isEquals;
    }
}
