public JSType caseObjectType(ObjectType type) {
  if (type.getDisplayName().equals("function")) {
    JSType ctorType = getNativeType(U2U_CONSTRUCTOR_TYPE);
    return resultEqualsValue && ctorType.isSubtype(type) ? ctorType : null;
  }
  return matchesExpectation("object") ? type : null;
}