public JSType caseObjectType(ObjectType type) {
  if (value.equals("function")) {
    // Objects are restricted to "Function", subtypes are left
    // Only filter out subtypes of "function"
    return matchesExpectation("function") ? getNativeType(U2U_CONSTRUCTOR_TYPE) : null;
      // Other types should be handled by the super method
  } else if (matchesExpectation("object")) {
    return super.caseObjectType(type);
  }
  return null;
}