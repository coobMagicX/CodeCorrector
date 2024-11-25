public JSType caseObjectType(ObjectType type) {
  if (value.equals("function")) {
    JSType ctorType = getNativeType(U2U_CONSTRUCTOR_TYPE);
    // Change the condition to check for exact equality, not subtypes
    return resultEqualsValue && ctorType == type ? ctorType : null;
  }
  return matchesExpectation("object") ? type : null;
}