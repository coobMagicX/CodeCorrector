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
            this.setEquals(false);
            return this;
        }
        
        Class<?> lhsClass = lhs.getClass();
        if (!lhsClass.isArray()) {
            // The simple case, not an array, just test the element
            isEquals = lhs.equals(rhs);
        } else if (!lhsClass.equals(rhs.getClass())) {
            // Here when we compare different dimensions, for example: a boolean[][] to a boolean[] 
            this.setEquals(false);
        } else {
            // 'Switch' on type of array, to dispatch to the correct handler
            // This handles multi dimensional arrays of the same depth
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
        return this;
    }

    private void append(long[] lhs, long[] rhs) {
        if (!isEquals || lhs.length != rhs.length) {
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

    private void append(int[] lhs, int[] rhs) {
        if (!isEquals || lhs.length != rhs.length) {
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

    private void append(short[] lhs, short[] rhs) {
        if (!isEquals || lhs.length != rhs.length) {
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

    private void append(char[] lhs, char[] rhs) {
        if (!isEquals || lhs.length != rhs.length) {
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

    private void append(byte[] lhs, byte[] rhs) {
        if (!isEquals || lhs.length != rhs.length) {
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

    private void append(double[] lhs, double[] rhs) {
        if (!isEquals || lhs.length != rhs.length) {
            setEquals(false);
            return;
        }
        for (int i = 0; i < lhs.length; i++) {
            if (Double.compare(lhs[i], rhs[i]) != 0) {
                setEquals(false);
                return;
            }
        }
    }

    private void append(float[] lhs, float[] rhs) {
        if (!isEquals || lhs.length != rhs.length) {
            setEquals(false);
            return;
        }
        for (int i = 0; i < lhs.length; i++) {
            if (Float.compare(lhs[i], rhs[i]) != 0) {
                setEquals(false);
                return;
            }
        }
    }

    private void append(boolean[] lhs, boolean[] rhs) {
        if (!isEquals || lhs.length != rhs.length) {
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

    private void append(Object[] lhs, Object[] rhs) {
        if (!isEquals || lhs.length != rhs.length) {
            setEquals(false);
            return;
        }
        for (int i = 0; i < lhs.length; i++) {
            append(lhs[i], rhs[i]);
        }
    }

    private void setEquals(boolean value) {
        isEquals = value;
    }

    public boolean isEquals() {
        return isEquals;
    }
}