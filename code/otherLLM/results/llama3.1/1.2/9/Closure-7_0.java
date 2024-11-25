public JSType caseObjectType(ObjectType type) {
  if (value.equals("function")) {
    JSType ctorType = getNativeType(U2U_CONSTRUCTOR_TYPE);
    // Check if the subtype of "Function" is present in the expected values
    return resultEqualsValue && matchesExpectation("Function") ? ctorType : null;
  }
  return matchesExpectation("object") ? type : null;
}