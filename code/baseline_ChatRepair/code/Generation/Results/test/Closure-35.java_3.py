private void inferPropertyTypesToMatchConstraint(JSType type, JSType constraint) {
  if (type == null || constraint == null) {
    return;
  }

  ObjectType constraintObj = ObjectType.cast(constraint.restrictByNotNullOrUndefined());
  if (constraintObj != null && constraintObj.isRecordType()) {
    ObjectType objType = ObjectType.cast(type.restrictByNotNullOrUndefined());
    if (objType != null) {
      for (String prop : constraintObj.getOwnPropertyNames()) {
        JSType propType = constraintObj.getPropertyType(prop);
        if (!objType.isPropertyTypeDeclared(prop)) {
          JSType typeToInfer = propType;
          if (!objType.hasProperty(prop)) {
            JSType voidType = getNativeType(VOID_TYPE);
            if (voidType.isSubtype(propType)) {
              typeToInfer = propType; // if propType already encompasses VOID_TYPE, use it
            } else {
              typeToInfer = voidType.getLeastSupertype(propType); // combine VOID_TYPE specifics with propType
            }
          }
          objType.defineInferredProperty(prop, typeToInfer, null);
        }
      }
    }
  }
}
