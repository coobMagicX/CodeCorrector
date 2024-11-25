public JSType caseObjectType(ObjectType type) {
  if (value.equals("function")) {
    JSType ctorType = getNativeType(U2U_CONSTRUCTOR_TYPE);
    // Correct handling of subtypes: Check if the input 'type' is a subtype of "function"
    return resultEqualsValue && type.isSubtype(ctorType) ? ctorType : null;
  }
  return matchesExpectation("object") ? type : null;
}