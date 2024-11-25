public JSType caseObjectType(ObjectType type) {
  if (value.equals("function")) {
    JSType ctorType = getNativeType(U2U_CONSTRUCTOR_TYPE);
    return !ctorType.isSubtype(type) ? ctorType : null; // Only filter out subtypes of "function"
  }
  return matchesExpectation("object") && type != U2U_CONSTRUCTOR_TYPE ? type : null;
}