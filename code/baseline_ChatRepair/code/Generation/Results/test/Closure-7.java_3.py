public JSType caseObjectType(ObjectType type) {
  if ("function".equals(value)) {
    JSType ctorType = getNativeType(U2U_CONSTRUCTOR_TYPE);
    return resultEqualsValue && (ctorType.isSubtype(type) || type.isFunctionPrototypeType()) ? ctorType : null;
  }
  // Improve condition to account for multiple possible types.
  if ("object".equals(value) || type.isSubtype(getNativeType(OBJECT_TYPE))) {
    return type;
  }
  return null;
}
