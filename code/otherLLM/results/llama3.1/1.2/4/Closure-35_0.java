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
        if (!objType.hasProperty(prop)) { // Modified condition to check property existence
          JSType typeToInfer = propType;
          if (!objType.isPropertyTypeDeclared(prop)) {
            if (propType != getNativeType(VOID_TYPE)) { // Added null check for VOID_TYPE
              typeToInfer =
                  getNativeType(VOID_TYPE).getLeastSupertype(propType);
            }
          }
          objType.defineInferredProperty(prop, typeToInfer, null);
        }
      }
    }
  }
}