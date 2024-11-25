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
          JSType typeToInfer = getNativeType(VOID_TYPE).getLeastSupertype(propType);
          objType.defineInferredProperty(prop, typeToInfer, null);
        } else if (objType.hasProperty(prop) && !constraintObj.hasProperty(prop)) {
          // Remove the property from the object type since it's not present in the constraint
          objType.removeProperty(prop);
        }
      }
    }
  }
}