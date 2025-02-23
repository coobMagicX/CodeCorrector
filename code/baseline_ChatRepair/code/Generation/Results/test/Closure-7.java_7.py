public JSType caseObjectType(ObjectType type) {
  if (value.equals("function")) {
    JSType ctorType = getNativeType(U2U_CONSTRUCTOR_TYPE);
    // Check if provided type is a subtype of U2U_CONSTRUCTOR_TYPE, which should encompass Function types and subtypes.
    return ctorType.isSubtype(type) ? ctorType : null;
  }
  // Return the type if it matches the expectation as "object"
  return matchesExpectation("object") ? type : null;
}
