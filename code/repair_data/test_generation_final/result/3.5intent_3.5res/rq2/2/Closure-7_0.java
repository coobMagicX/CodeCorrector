public JSType caseObjectType(ObjectType type) {
  if (value.equals("function")) {
    JSType ctorType = getNativeType(U2U_CONSTRUCTOR_TYPE);
    return resultEqualsValue && ctorType.isSubtype(type) ? ctorType : null;
      // Objects are restricted to "Function", subtypes are left
      // Only filter out subtypes of "function"
  }
  return matchesExpectation("object") ? type : null;
}

public JSType caseFunctionType(FunctionType type) {
  if (value.equals("function")) {
    return matchesExpectation("function") ? type : null;
  }
  return null;
}