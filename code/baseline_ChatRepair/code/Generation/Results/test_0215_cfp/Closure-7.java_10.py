public JSType caseObjectType(ObjectType type) {
  if (value.equals("function")) {
    JSType functionType = getNativeType(JSTypeNative.FUNCTION_PROTOTYPE);
    return type.isFunctionPrototypeType() || (functionType.isEquivalentTo(type) && resultEqualsValue) ? functionType : null;
  }
  return matchesExpectation("object") ? type : null;
}
