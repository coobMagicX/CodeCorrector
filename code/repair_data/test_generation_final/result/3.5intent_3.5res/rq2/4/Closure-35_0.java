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
            typeToInfer = getNativeType(VOID_TYPE).getLeastSupertype(propType);
          }
          objType.defineInferredProperty(prop, typeToInfer, null);
        }
      }
    }
  }
  else if (constraintObj != null && !constraintObj.isRecordType()) {
    ObjectType objType = ObjectType.cast(type.restrictByNotNullOrUndefined());
    if (objType != null) {
      for (String prop : objType.getOwnPropertyNames()) {
        if (!constraintObj.isPropertyTypeDeclared(prop)) {
          JSType typeToInfer = getNativeType(VOID_TYPE).getLeastSupertype(objType.getPropertyType(prop));
          constraintObj.defineInferredProperty(prop, typeToInfer, null);
        }
      }
    }
  }
  else {
    ObjectType objType = ObjectType.cast(type.restrictByNotNullOrUndefined());
    if (objType != null) {
      for (String prop : objType.getOwnPropertyNames()) {
        if (!objType.isPropertyTypeDeclared(prop)) {
          JSType typeToInfer = getNativeType(VOID_TYPE).getLeastSupertype(objType.getPropertyType(prop));
          objType.defineInferredProperty(prop, typeToInfer, null);
        }
      }
    }
  }
}