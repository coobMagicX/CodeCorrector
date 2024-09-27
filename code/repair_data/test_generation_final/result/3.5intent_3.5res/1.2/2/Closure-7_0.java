public JSType caseObjectType(ObjectType type) {
    if (value.equals("function")) {
        JSType ctorType = getNativeTypeForTypeOf("function");
        if (ctorType != null && ctorType.isSubtype(type)) {
            return ctorType;
        }
    } else if (matchesExpectation("object")) {
        return type;
    }
    return null;
}