public JSType caseObjectType(ObjectType type) {
    if (matchesExpectation("function")) {
        JSType ctorType = getNativeType(JSTypeNative.U2U_CONSTRUCTOR_TYPE);
        return resultEqualsValue && ctorType.isSubtype(type) ? ctorType : null;
        // Objects are restricted to "Function", subtypes are left
        // Only filter out subtypes of "function"
    }
    return matchesExpectation("object") ? type : null;
}