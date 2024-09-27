public JSType caseObjectType(ObjectType type) {
  if (value.equals("function")) {
    JSType ctorType = getNativeTypeForTypeOf("function");
    return ctorType != null && ctorType.isSubtype(type) ? ctorType : null;
  }
  return matchesExpectation("object") ? type : null;
}