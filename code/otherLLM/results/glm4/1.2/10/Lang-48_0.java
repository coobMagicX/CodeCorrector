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
    Class<?> rhsClass = rhs.getClass();

    if (!lhsClass.equals(rhsClass)) {
        this.setEquals(false);
        return this;
    }

    if (lhs instanceof BigDecimal && rhs instanceof BigDecimal) {
        BigDecimal lhsBigDecimal = (BigDecimal) lhs;
        BigDecimal rhsBigDecimal = (BigDecimal) rhs;
        // Correct comparison using BigDecimal's equals, which compares the values
        isEquals = lhsBigDecimal.equals(rhsBigDecimal);
    } else {
        // For non-BigDecimal types, use reflectionEquals to compare recursively
        isEquals = reflectionEquals(lhs, rhs, true, null, null);
    }

    return this;
}

// Method used for comparison of non-BigDecimal objects using reflection.
private static boolean reflectionEquals(Object lhs, Object rhs, boolean testTransients, Class<?> reflectUpToClass,
        String[] excludeFields) {
    if (lhs == rhs) {
        return true;
    }
    if (lhs == null || rhs == null) {
        return false;
    }
    // Find the leaf class since there may be transients in the leaf 
    // class or in classes between the leaf and root.
    Class<?> lhsClass = lhs.getClass();
    Class<?> rhsClass = rhs.getClass();
    Class<?> testClass;
    if (lhsClass.isInstance(rhs)) {
        testClass = lhsClass;
        if (!rhsClass.isInstance(lhs)) {
            // rhsClass is a subclass of lhsClass
            testClass = rhsClass;
        }
    } else if (rhsClass.isInstance(lhs)) {
        testClass = rhsClass;
        if (!lhsClass.isInstance(rhs)) {
            // lhsClass is a subclass of rhsClass
            testClass = lhsClass;
        }
    } else {
        // The two classes are not related.
        return false;
    }
    EqualsBuilder equalsBuilder = new EqualsBuilder();
    try {
        reflectionAppend(lhs, rhs, testClass, equalsBuilder, testTransients, excludeFields);
        while (testClass.getSuperclass() != null && testClass != reflectUpToClass) {
            testClass = testClass.getSuperclass();
            reflectionAppend(lhs, rhs, testClass, equalsBuilder, testTransients, excludeFields);
        }
    } catch (IllegalArgumentException e) {
        // In this case, we tried to test a subclass vs. a superclass and
        // the subclass has ivars or the ivars are transient and 
        // we are testing transients.
        // If a subclass has ivars that we are trying to test them, we get an
        // exception and we know that the objects are not equal.
        return false;
    }
    return equalsBuilder.isEquals();
}