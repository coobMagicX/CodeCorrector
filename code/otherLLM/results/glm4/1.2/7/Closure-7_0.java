public JSType caseObjectType(ObjectType type) {
  if (value.equals("function")) {
    JSType ctorType = getNativeType(U2U_CONSTRUCTOR_TYPE);
    return resultEqualsValue && ctorType.isSubtype(type) ? type : null;
    // Objects are restricted to "Function", subtypes are left
    // Correctly handle the case where 'type' is exactly 'ctorType'
  } else if (matchesExpectation("object")) {
    return type;
  }
  return null; // Return null for all other cases or when value is not "function"
}