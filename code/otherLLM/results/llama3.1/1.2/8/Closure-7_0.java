public JSType caseObjectType(ObjectType type) {
  if (value.equals("function")) {
    JSType ctorType = getNativeType(U2U_CONSTRUCTOR_TYPE);
    // Only filter out subtypes of "function"
    return !ctorType.isSubtype(type) ? ctorType : null;
  }
  return matchesExpectation("object") ? type : null;
}