public JSType caseObjectType(ObjectType type) {
    if (value.equals("function")) {
        JSType ctorType = getNativeTypeForTypeOf(value); // fixed to use the appropriate method
        return resultEqualsValue && ctorType != null && ctorType.isSubtype(type) ? ctorType : null;
        // Ensuring ctorType is not null before checking isSubtype
        // Objects are restricted to "Function", subtypes are left
        // Only filter out subtypes of "function"
    }
    return matchesExpectation("object") ? type : null;
}