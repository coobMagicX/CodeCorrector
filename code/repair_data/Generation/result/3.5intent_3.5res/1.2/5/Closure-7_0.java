public JSType caseObjectType(ObjectType type) {
  if ((value.equals("function") || value.equals(U2U_CONSTRUCTOR_TYPE)) && matchesExpectation("function")) {
    JSType ctorType = getNativeType(U2U_CONSTRUCTOR_TYPE);
    return resultEqualsValue && ctorType.isSubtype(type) ? ctorType : null;
  }
  return matchesExpectation("object") ? type : null;
}