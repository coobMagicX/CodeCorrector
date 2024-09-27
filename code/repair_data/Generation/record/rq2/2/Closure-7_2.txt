public JSType caseObjectType(ObjectType type) {
  if (matchesExpectation("function")) {
    JSType ctorType = getNativeType(U2U_CONSTRUCTOR_TYPE);
    return ctorType.isSubtype(type) ? ctorType : null;
  }
  return matchesExpectation("object") ? type : null;
}