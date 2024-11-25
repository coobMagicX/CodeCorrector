public JSType caseObjectType(ObjectType type) {
  if (value.equals("function")) {
    JSType ctorType = getNativeType(U2U_CONSTRUCTOR_TYPE);
    // Filter out subtypes of "function" by checking if the given type is a subtype of "function"
    return !type.isSubtype(getNativeType(U2U_FUNCTION_TYPE)) ? 
           resultEqualsValue && ctorType.isSubtype(type) ? ctorType : null : null;
  }
  return matchesExpectation("object") ? type : null;
}