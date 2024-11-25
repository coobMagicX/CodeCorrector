public JSType caseObjectType(ObjectType type) {
    if ("function".equals(type.getName())) {
        JSType ctorType = getNativeType(U2U_CONSTRUCTOR_TYPE);
        // Use a more descriptive variable name for clarity
        boolean isExpectedFunction = resultEqualsValue && ctorType.isSubtype(type);
        return isExpectedFunction ? ctorType : null;
    }
    // For other object types, check if they match the expectation "object"
    return matchesExpectation("object") ? type : null;
}