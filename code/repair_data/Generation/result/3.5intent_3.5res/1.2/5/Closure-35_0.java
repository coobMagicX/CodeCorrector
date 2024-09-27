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
          if (!objType.hasProperty(prop)) {
            typeToInfer =
                getNativeType(VOID_TYPE).getLeastSupertype(propType);
          }
          objType.defineInferredProperty(prop, typeToInfer, null);
        } else if (!objType.hasProperty(prop)) {
          objType.defineInferredProperty(prop, propType, null);
        } else {
          JSType currentPropType = objType.getPropertyType(prop);
          if (!currentPropType.equals(propType)) {
            JSType mergedType = currentPropType.getLeastSupertype(propType);
            objType.defineInferredProperty(prop, mergedType, null);
          }
        }
      }
    }
  }
}