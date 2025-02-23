public JSType caseObjectType(ObjectType type) {
  if (value.equals("function")) {
    JSType functionType = getNativeType(JSTypeNative.FUNCTION_INSTANCE_TYPE);
    if (type.isFunctionType() || type.isSubtype(functionType)) {
      return resultEqualsValue ? functionType : null;
    } else {
      return null;
    }
  } else if (matchesExpectation("object")) {
    return type;
  }
  return null;
}
