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
    } else if (lhs.getClass() != rhs.getClass()) {
        // Here when we compare different dimensions, for example: a boolean[][] to a boolean[]
        this.setEquals(false);
    }
    
    // 'Switch' on type of object, to dispatch to the correct handler
    // This handles multi dimensional arrays of the same depth and non-array objects
    if (lhs instanceof BigDecimal) {
        isEquals = lhs.equals(rhs);
    } else if (lhsClass == rhsClass) {
        switch (lhsClass.getName()) {
            case "java.lang.String":
                appendSuper(lhs.toString().equals(rhs.toString()));
                break;
            case "java.lang.Integer":
            case "java.math.BigDecimal":
                // For BigDecimal, use compareTo to compare values directly
                isEquals = ((BigDecimal) lhs).compareTo((BigDecimal) rhs) == 0;
                break;
            // Other types can be appended here as necessary
        }
    } else {
        this.setEquals(false);
    }

    return this;
}