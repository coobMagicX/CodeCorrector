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
        JSType propType = caseTopType(constraintObj.getPropertyType(prop));
        if (!objType.isPropertyTypeDeclared(prop)) {
          JSType typeToInfer = propType;
          if (!objType.hasProperty(prop)) {
            typeToInfer =
                getNativeType(VOID_TYPE).getLeastSupertype(propType);
          }
          objType.defineInferredProperty(prop, typeToInfer, null);
        } else {
          JSType existingPropType = objType.getPropertyType(prop);
          if (!existingPropType.equals(propType)) {
            typeToInfer =
                getNativeType(VOID_TYPE).getLeastSupertype(propType);
            objType.defineInferredProperty(prop, typeToInfer, null);
          }
        }
      }
    }
  }
}