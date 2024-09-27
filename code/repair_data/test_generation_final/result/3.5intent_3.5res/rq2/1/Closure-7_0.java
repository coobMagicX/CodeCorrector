public JSType caseObjectType(ObjectType type) {
  if (value.equals("function")) {
    JSType ctorType = getNativeTypeForTypeOf("function");
    return resultEqualsValue && ctorType != null && ctorType.isSubtype(type) ? ctorType : null;
  } else {
    return null;
  }
}