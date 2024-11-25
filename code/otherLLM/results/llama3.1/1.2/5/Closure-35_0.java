private void inferPropertyTypesToMatchConstraint(
    JSType type, JSType constraint) {
  if (type == null || constraint == null) {
    return;
  }

  ObjectType constraintObj =
      ObjectType.cast(constraint.restrictByNotNullOrUndefined());
  if (constraintObj != null && constraintObj.isRecordType()) {
    ObjectType objType = ObjectType.cast(type.restrictByNotNullOrUndefined());
    if (objType != null) {
      for (String prop : constraintObj.getOwnPropertyNames()) {
        JSType propType = constraintObj.getPropertyType(prop);
        if (!objType.isPropertyTypeDeclared(prop)) {
          JSType typeToInfer = propType;
          // Fix: Check if the type is primitive and handle subtyping relationships correctly
          if (typeToInfer.getNativeType() == BOOLEAN_TYPE) {
            typeToInfer = getNativeType(BOOLEAN_TYPE);
          } else if (typeToInfer.getNativeType() == NUMBER_TYPE) {
            typeToInfer = getNativeType(NUMBER_TYPE);
          } else if (typeToInfer.getNativeType() == STRING_TYPE) {
            typeToInfer = getNativeType(STRING_TYPE);
          }
          // Fix: Handle subtyping relationships between primitive types
          if (!objType.hasProperty(prop)) {
            JSType leastSupertype = getNativeType(VOID_TYPE).getLeastSupertype(typeToInfer);
            typeToInfer = leastSupertype;
          }
          objType.defineInferredProperty(prop, typeToInfer, null);
        }
      }
    }
  }
}