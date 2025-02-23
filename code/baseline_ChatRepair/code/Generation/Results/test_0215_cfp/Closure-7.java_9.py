public JSType caseObjectType(ObjectType type) {
  if (value.equals("function")) {
    JSType ctorType = getNativeType(U2U_CONSTRUCTOR_TYPE);
    // Use the more general isFunctionType() method on the type
    if (type.isFunctionType()) {
      return resultEqualsValue ? ctorType : null;
    }
  }
  return matchesExpectation("object") ? type : null;
}
