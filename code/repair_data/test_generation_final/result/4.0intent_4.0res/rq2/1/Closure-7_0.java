public JSType caseObjectType(ObjectType type) {
    if (type instanceof FunctionType) {
        return caseFunctionType((FunctionType) type);
    } else if (matchesExpectation("function")) {
        JSType ctorType = getNativeType(U2U_CONSTRUCTOR_TYPE);
        return ctorType != null && ctorType.isSubtype(type) ? ctorType : null;
    }
    return matchesExpectation("object") ? type : null;
}