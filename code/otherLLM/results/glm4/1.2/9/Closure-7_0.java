public JSType caseObjectType(ObjectType type) {
  if (value.equals("function")) {
    // Correctly determine the constructor type based on ObjectType
    JSType constructorType = getNativeType(U2U_CONSTRUCTOR_TYPE);

    // If the object is a "Function", check for subtype relationship with constructorType
    if (type instanceof FunctionType) {
      return resultEqualsValue && constructorType.isSubtype(type) ? constructorType : null;
    }
    // For subtypes of "function", do not filter them out as per requirement
  }
  return matchesExpectation("object") ? type : null;
}