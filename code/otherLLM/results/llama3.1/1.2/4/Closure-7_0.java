public JSType caseObjectType(ObjectType type) {
  if (value.equals("function")) {
    JSType ctorType = getNativeType(U2U_CONSTRUCTOR_TYPE);
    // If the type is a subtype of "function", return it, otherwise null
    return typeRegistry.isSubtype(type, getNativeType("Function")) ? getNativeType("Function") : null;
  }
  
  // Filter out subtypes of "object" and leave only "object"
  if (matchesExpectation("object")) {
    JSType objType = getNativeType("Object");
    return typeRegistry.isSubtype(type, objType) ? objType : null;
  }

  return null;
}