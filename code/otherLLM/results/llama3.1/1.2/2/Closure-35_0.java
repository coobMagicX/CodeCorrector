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
        if (!objType.hasProperty(prop)) {
          JSType typeToInfer = propType;
          if (!objType.isPropertyTypeDeclared(prop)) {
            typeToInfer =
                getNativeType(VOID_TYPE).getLeastSupertype(propType);
          }
          objType.defineInferredProperty(prop, typeToInfer, null);
        } else if (propType.hasTypeParameters()) {
          JSType inferredPropType = inferPropertyTypesToMatchConstraint(
              propType, objType.getPropertyType(prop));
          objType.updatePropertyType(prop, inferredPropType);
        }
      }
    }
  }
}