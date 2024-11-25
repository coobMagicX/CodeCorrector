public EqualsBuilder append(Object lhs, Object rhs) {
    if (isEquals == false) {
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
    } else if (lhsClass != rhs.getClass()) {
        // Here when we compare different dimensions, for example: a boolean[][] to a boolean[] 
        this.setEquals(false);
    } else {
        int length = Array.getLength(lhs);
        if (rhs instanceof BigDecimal[]) {
            // Special handling for BigDecimal arrays
            BigDecimal[] lhsBigDecimal = (BigDecimal[]) lhs;
            BigDecimal[] rhsBigDecimal = (BigDecimal[]) rhs;
            for (int i = 0; i < length; i++) {
                isEquals &= lhsBigDecimal[i].compareTo(rhsBigDecimal[i]) == 0;
            }
        } else if (!lhsClass.getComponentType().isPrimitive()) {
            // Not a primitive array, treat as Object[]
            Object[] lhsObjectArray = (Object[]) lhs;
            Object[] rhsObjectArray = (Object[]) rhs;
            for (int i = 0; i < length; i++) {
                append(lhsObjectArray[i], rhsObjectArray[i]);
            }
        } else {
            // Handle primitive arrays
            if (lhsClass == boolean[].class) {
                this.isEquals &= Arrays.equals((boolean[]) lhs, (boolean[]) rhs);
            } else if (lhsClass == byte[].class) {
                this.isEquals &= Arrays.equals((byte[]) lhs, (byte[]) rhs);
            } else if (lhsClass == char[].class) {
                this.isEquals &= Arrays.equals((char[]) lhs, (char[]) rhs);
            } else if (lhsClass == short[].class) {
                this.isEquals &= Arrays.equals((short[]) lhs, (short[]) rhs);
            } else if (lhsClass == int[].class) {
                this.isEquals &= Arrays.equals((int[]) lhs, (int[]) rhs);
            } else if (lhsClass == long[].class) {
                this.isEquals &= Arrays.equals((long[]) lhs, (long[]) rhs);
            } else if (lhsClass == float[].class) {
                this.isEquals &= Arrays.equals((float[]) lhs, (float[]) rhs);
            } else if (lhsClass == double[].class) {
                this.isEquals &= Arrays.equals((double[]) lhs, (double[]) rhs);
            }
        }
    }
    return this;
}