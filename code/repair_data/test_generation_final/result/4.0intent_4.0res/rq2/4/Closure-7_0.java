public JSType caseObjectType(ObjectType type) {
    if (value.equals("function")) {
        JSType ctorType = getNativeType(U2U_CONSTRUCTOR_TYPE);
        // Ensure object is a subtype of U2U_CONSTRUCTOR_TYPE when value is "function"
        // Additionally, check if the type itself is considered a function to accommodate new test case
        return type.isFunctionType() || ctorType.isSubtype(type) ? ctorType : null;
    }
    // Return type if the expected value is "object" and the type matches the expectation
    return matchesExpectation("object") ? type : null;
}