private void inferPropertyTypesToMatchConstraint(JSType type, JSType constraint) {
  if (type == null || constraint == null) {
    return;
  }

  ObjectType constraintObj = ObjectType.cast(constraint.restrictByNotNullOrUndefined());
  if (constraintObj != null && constraintObj.isRecordType()) {
    ObjectType objType = ObjectType.cast(type.restrictByNotNullOrUndefined());
    if (objType != null) {
      for (String prop : constraintObj.getOwnPropertyNames()) {
        if (!objType.isPropertyTypeInferred(prop)) {
          JSType propType = constraintObj.getPropertyType(prop);
          objType.defineInferredProperty(prop, propType, null);
        }
      }
    }
  }
}
