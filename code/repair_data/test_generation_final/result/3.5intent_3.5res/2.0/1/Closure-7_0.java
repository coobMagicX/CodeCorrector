public JSType caseObjectType(ObjectType type) {
  if (value.equals("function") || value.equals(U2U_CONSTRUCTOR_TYPE)) {
    JSType ctorType = getNativeType(U2U_CONSTRUCTOR_TYPE);
    if (resultEqualsValue && ctorType.isSubtype(type)) {
      return ctorType;
    } else if (!resultEqualsValue && type.isSubtype(ctorType)) {
      return null;
    }
    return type;
  }
  
  return matchesExpectation("object") ? type : null;
}
