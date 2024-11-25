public JSType caseObjectType(ObjectType type) {
  // Check if the type is a subtype of "Function" and not just "function"
  if ("Function".equals(type.getName())) {
    JSType ctorType = getNativeType(U2U_CONSTRUCTOR_TYPE);
    return resultEqualsValue && ctorType.isSubtype(type) ? ctorType : null;
  }
  // If the type is not a function or its subtype, handle it accordingly
  else if (matchesExpectation("object")) {
    JSType topType = caseTopType(type); // Use the provided method to get the top type
    return topType != null ? topType : null;
  }
  return null; // If it's neither a function nor an object, return null
}