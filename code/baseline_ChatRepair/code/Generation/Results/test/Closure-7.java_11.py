public JSType caseObjectType(ObjectType type) {
  if (value.equals("function")) {
    JSType functionType = getNativeType(JSTypeNative.FUNCTION_INSTANCE_TYPE);
    // Check if the type is either a subtype of the function type or the function type itself
    if (type.isSubtype(functionType)) {
      return resultEqualsValue ? functionType : null;
    }
  }
  return matchesExpectation("object") ? type : null;
}
