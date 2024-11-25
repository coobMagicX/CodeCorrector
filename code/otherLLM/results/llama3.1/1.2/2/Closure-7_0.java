public JSType caseObjectType(ObjectType type) {
  if (value.equals("function")) {
    JSType ctorType = getNativeType(U2U_CONSTRUCTOR_TYPE);
    // Corrected: Return the subtype of "function" when called with the input "goog.isFunction"
    return resultEqualsValue && !matchesExpectation("function") ? ctorType : null;
  }
  return matchesExpectation("object") ? type : null;
}