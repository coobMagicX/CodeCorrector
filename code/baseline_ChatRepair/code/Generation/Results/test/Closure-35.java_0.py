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
          if (!objType.hasProperty(prop)) {
            objType.defineInferredProperty(prop, propType, null);
          } else {
            JSType existingType = objType.getPropertyType(prop);
            JSType typeToInfer = existingType.getLeastSupertype(propType);
            objType.defineInferredProperty(prop, typeToInfer, null);
          }
        }
      }
    }
  }
}
