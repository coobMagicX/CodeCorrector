public JSType caseObjectType(ObjectType type) {
    if (value != null && value.equals("function")) {
        JSType ctorType = getNativeType(U2U_CONSTRUCTOR_TYPE);
        if (ctorType != null) {
            boolean resultEqualsValue = U2U_CONSTRUCTOR_TYPE.equals(value);
            // Objects are restricted to "Function", subtypes are left
            // Only filter out subtypes of "function"
            return resultEqualsValue && ctorType.isSubtype(type) ? ctorType : null;
        }
    } else if (matchesExpectation("object")) {
        return type; // Correctly returns the object type for non-function objects
    }

    // Return null for other types or conditions that are not specified
    return null;
}